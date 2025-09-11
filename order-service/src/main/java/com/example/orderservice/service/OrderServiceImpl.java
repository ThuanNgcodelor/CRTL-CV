package com.example.orderservice.service;

import com.example.orderservice.client.StockServiceClient;
import com.example.orderservice.dto.*;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.DecreaseStockRequest;
import com.example.orderservice.request.SendNotificationRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.apache.kafka.clients.admin.NewTopic;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StockServiceClient stockServiceClient;
    private final NewTopic orderTopic;
    private final NewTopic notificationTopic;
    private final KafkaTemplate<String, CartDto> kafkaTemplate;
    private final KafkaTemplate<String, SendNotificationRequest> kafkaTemplateSend;
    private static final Logger log = LoggerFactory.getLogger(OrderServiceImpl.class);

    @Transactional
    @Override
    public Order placeOrder(HttpServletRequest request) {
        String author = request.getHeader("Authorization");
        CartDto cartDto = stockServiceClient.getCart(author).getBody();

        if (cartDto == null) {
            throw new RuntimeException("Cart not found or empty");
        }

        Order order = createOrder(cartDto);

        List<OrderItem> orderItems = createOrderItems(order, cartDto);
        order.setOrderItems(orderItems);
        order.setTotalPrice(calculateTotalPrice(orderItems));
        stockServiceClient.clearCartByCartId(cartDto.getId());

        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(cartDto.getUserId())
                .orderId(order.getId())
                .message("Order placed successfully with ID: " + order.getId())
                .build();
//        log.info("Kafka send to topic [{}]: {}", orderTopic.name(), notification);
//        kafkaTemplate.send(orderTopic.name(), notification);

        return orderRepository.save(order);
//        return null;
    }

    @Transactional
    @Override
    public void placeOrderKafka(HttpServletRequest request) {
        String author = request.getHeader("Authorization");
        CartDto cartDto = stockServiceClient.getCart(author).getBody();
        if(cartDto == null || cartDto.getItems().isEmpty())
            throw new RuntimeException("Cart not found or empty");

        log.info("Kafka send to topic [{}]: {}", orderTopic.name(), cartDto);
        cartDto.setAuthor(author);

        kafkaTemplate.send(orderTopic.name(),cartDto);
    }

    @KafkaListener(topics = "#{@orderTopic.name}", groupId = "order-service-group")
    @Transactional
    @Override
    public void consumeCartAndCreateOrder(CartDto cartDto) {
        log.info("Received cart to process: {}", cartDto.getId());
        Order order = createOrder(cartDto);
        List<OrderItem> orderItems = createOrderItems(order, cartDto);
        order.setOrderItems(orderItems);
        order.setTotalPrice(calculateTotalPrice(orderItems));

//        List<String> productIds = cartDto.getItems().stream()

//                .map(CartItemDto::getProductId)
//                .toList();
//        RemoveCartItemRequest request = new RemoveCartItemRequest();
//        request.setCartId(cartDto.getId());
//        request.setProductIds(productIds);
//        stockServiceClient.removeCartItems(request);
        stockServiceClient.clearCartByCartId(cartDto.getId());
        log.info("Clearing cart with ID: {}", cartDto.getId());
        SendNotificationRequest notification = SendNotificationRequest.builder()
                .userId(cartDto.getUserId())
                .orderId(order.getId())
                .message("Order placed successfully with ID: " + order.getId())
                .build();

        kafkaTemplateSend.send(notificationTopic.name(), notification);
        orderRepository.save(order);
    }

    private Order createOrder(CartDto cartDto){
        Order order = Order.builder()
                .userId(cartDto.getUserId())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(0.0)
                .build();
        return orderRepository.save(order);
    }

    private List<OrderItem> createOrderItems(Order order, CartDto cartDto){
        return cartDto.getItems().stream().map(cartItemDto -> {
            ProductDto productDto = stockServiceClient.getProductById(cartItemDto.getProductId()).getBody();
            if(productDto == null)
                throw new RuntimeException("Product not found for ID: " + cartItemDto.getProductId());

            DecreaseStockRequest request = new DecreaseStockRequest();
            request.setProductId(cartItemDto.getProductId());
            request.setQuantity(cartItemDto.getQuantity());
            stockServiceClient.decreaseStock(request);

            log.info("Stock decreased for product ID: {}", cartItemDto.getProductId());

            return OrderItem.builder()
                    .productId(cartItemDto.getProductId())
                    .quantity(cartItemDto.getQuantity())
                    .unitPrice(cartItemDto.getUnitPrice())
                    .totalPrice(cartItemDto.getTotalPrice())
                    .order(order)
                    .build();
        }).collect(Collectors.toList());
    }

    protected double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
    }

    @Override
    public CartDto getCartByUserId(HttpServletRequest request) {
        String author = request.getHeader("Authorization");
        return stockServiceClient.getCart(author).getBody();
    }

    @Override
    public Order getOrderById(String orderId) {
        return orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return orderRepository.findByUserId(userId);
    }

    // CRUD Implementation

    @Override
    @Transactional
    public Order createOrder(CreateOrderRequest request) {
        Order order = Order.builder()
                .userId(request.getUserId())
                .orderStatus(OrderStatus.PENDING)
                .totalPrice(0.0)
                .build();
        
        final Order savedOrder = orderRepository.save(order);
        
        List<OrderItem> orderItems = request.getOrderItems().stream()
                .map(itemRequest -> {
                    OrderItem orderItem = OrderItem.builder()
                            .productId(itemRequest.getProductId())
                            .quantity(itemRequest.getQuantity())
                            .unitPrice(itemRequest.getUnitPrice())
                            .totalPrice(itemRequest.getUnitPrice() * itemRequest.getQuantity())
                            .order(savedOrder)
                            .build();
                    return orderItem;
                }).collect(Collectors.toList());
        
        savedOrder.setOrderItems(orderItems);
        savedOrder.setTotalPrice(calculateTotalPrice(orderItems));
        
        return orderRepository.save(savedOrder);
    }

    @Override
    public List<Order> getAllOrders(String status) {
        if (status != null && !status.isEmpty()) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByOrderStatus(orderStatus);
        }
        return orderRepository.findAll();
    }

    @Override
    @Transactional
    public Order updateOrder(String orderId, UpdateOrderRequest request) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
        
        if (request.getOrderStatus() != null) {
            order.setOrderStatus(OrderStatus.valueOf(request.getOrderStatus().toUpperCase()));
        }
        
        // Update other fields if provided
        // Note: In a real implementation, you might want to add these fields to the Order entity
        
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public Order updateOrderStatus(String orderId, String status) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
        
        order.setOrderStatus(OrderStatus.valueOf(status.toUpperCase()));
        return orderRepository.save(order);
    }

    @Override
    @Transactional
    public void deleteOrder(String orderId) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() -> new RuntimeException("Order not found for ID: " + orderId));
        
        orderRepository.delete(order);
    }

    @Override
    public List<Order> searchOrders(String userId, String status, String startDate, String endDate) {
        // This is a simplified implementation
        // In a real application, you would use a more sophisticated query builder
        if (userId != null && !userId.isEmpty()) {
            return orderRepository.findByUserId(userId);
        }
        
        if (status != null && !status.isEmpty()) {
            OrderStatus orderStatus = OrderStatus.valueOf(status.toUpperCase());
            return orderRepository.findByOrderStatus(orderStatus);
        }
        
        return orderRepository.findAll();
    }

    @Override
    public OrderStatisticsDto getOrderStatistics(String startDate, String endDate) {
        List<Order> orders;
        
        if (startDate != null && endDate != null) {
            LocalDateTime start = LocalDateTime.parse(startDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            LocalDateTime end = LocalDateTime.parse(endDate, DateTimeFormatter.ISO_LOCAL_DATE_TIME);
            orders = orderRepository.findByCreationTimestampBetween(start, end);
        } else {
            orders = orderRepository.findAll();
        }
        
        OrderStatisticsDto statistics = new OrderStatisticsDto();
        statistics.setTotalOrders((long) orders.size());
        statistics.setTotalRevenue(orders.stream().mapToDouble(Order::getTotalPrice).sum());
        statistics.setAverageOrderValue(orders.isEmpty() ? 0.0 : 
            orders.stream().mapToDouble(Order::getTotalPrice).average().orElse(0.0));
        
        // Group by status
        Map<String, Long> ordersByStatus = orders.stream()
                .collect(Collectors.groupingBy(
                    order -> order.getOrderStatus().toString(),
                    Collectors.counting()
                ));
        statistics.setOrdersByStatus(ordersByStatus);
        
        // Group by month
        Map<String, Long> ordersByMonth = orders.stream()
                .collect(Collectors.groupingBy(
                    order -> String.valueOf(order.getCreationTimestamp().getMonthValue()),
                    Collectors.counting()
                ));
        statistics.setOrdersByMonth(ordersByMonth);
        
        return statistics;
    }

}
