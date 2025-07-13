package com.example.orderservice.service;

import com.example.orderservice.client.StockServiceClient;
import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.DecreaseStockRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StockServiceClient stockServiceClient;

//    @Transactional
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
//        stockServiceClient.clearCartByCartId(cartDto.getId());
        return orderRepository.save(order);
    }

    protected Order createOrder(CartDto cartDto){
        Order order = Order.builder()
                .userId(cartDto.getUserId())
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
                .build();
        return orderRepository.save(order);
    }

    protected List<OrderItem> createOrderItems(Order order, CartDto cartDto){
        return cartDto.getItems().stream().map(cartItemDto -> {
            ProductDto productDto = stockServiceClient.getProductById(cartItemDto.getProductId()).getBody();
            if(productDto == null)
                throw new RuntimeException("Product not found for ID: " + cartItemDto.getProductId());

            DecreaseStockRequest request = new DecreaseStockRequest();
            request.setProductId(cartItemDto.getProductId());
            request.setQuantity(cartItemDto.getQuantity());
            stockServiceClient.decreaseStock(request);

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
        return null;
    }

    @Override
    public List<Order> getUserOrders(String userId) {
        return List.of();
    }

}
