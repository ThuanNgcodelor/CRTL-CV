package com.example.orderservice.service;

import com.example.orderservice.client.StockServiceClient;
import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.model.Order;
import com.example.orderservice.model.OrderItem;
import com.example.orderservice.repository.OrderRepository;
import com.example.orderservice.request.DecreaseStockRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final StockServiceClient stockServiceClient;

    @Transactional
    @Override
    public Order placeOrder(String userId) {
        CartDto cartDto = stockServiceClient.getCart(userId).getBody();
        assert cartDto != null;
        Order order = createOrder(cartDto);
        List<OrderItem> orderItems = createOrderItems(order, cartDto);
        order.setOrderItems(orderItems);
        order.setTotalPrice(calculateTotalPrice(orderItems));
        stockServiceClient.clearCartByCartId(cartDto.getId());
        return orderRepository.save(order);
    }

    private Order createOrder(CartDto cartDto){
        Order order = Order.builder()
                .userId(cartDto.getUserId())
                .orderStatus(OrderStatus.PENDING)
                .createdAt(LocalDateTime.now())
                .updatedAt(LocalDateTime.now())
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
            ResponseEntity<?> response = stockServiceClient.decreaseStock(request);
            if (!response.getStatusCode().is2xxSuccessful())
                throw new RuntimeException("Failed to decrease stock for product: " + cartItemDto.getProductId());

            return OrderItem.builder()
                    .productId(cartItemDto.getProductId())
                    .quantity(cartItemDto.getQuantity())
                    .unitPrice(cartItemDto.getUnitPrice())
                    .totalPrice(cartItemDto.getTotalPrice())
                    .orderId(order.getId())
                    .build();
        }).toList();
    }

    private double calculateTotalPrice(List<OrderItem> orderItems) {
        return orderItems.stream()
                .mapToDouble(item -> item.getUnitPrice() * item.getQuantity())
                .sum();
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
