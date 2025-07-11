package com.example.orderservice.dto;

import com.example.orderservice.enums.OrderStatus;
import com.example.orderservice.model.OrderItem;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class OrderDto {
    private String id;
    private String userId;
    private double totalPrice;
    private OrderStatus orderStatus;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private List<OrderItem> orderItems;
}
