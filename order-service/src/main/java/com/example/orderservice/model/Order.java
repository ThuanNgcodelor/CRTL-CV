package com.example.orderservice.model;

import com.example.orderservice.enums.OrderStatus;
import jakarta.persistence.Entity;
import lombok.*;
import org.hibernate.annotations.CreationTimestamp;

import java.time.LocalDateTime;
import java.util.List;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class Order extends BaseEntity {
    private String userId;
    private double totalPrice;
    private OrderStatus orderStatus;
    private List<OrderItem> orderItems;
    @CreationTimestamp
    private LocalDateTime createdAt;
    @CreationTimestamp
    private LocalDateTime updatedAt;
}
