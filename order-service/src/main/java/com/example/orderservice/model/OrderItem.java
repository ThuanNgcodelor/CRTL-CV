package com.example.orderservice.model;

import jakarta.persistence.Entity;
import lombok.*;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@Entity
public class OrderItem extends BaseEntity {
    private String orderId;
    private String productId;
    private int quantity;
    private  double unitPrice;
    private double totalPrice;
}
