package com.example.orderservice.dto;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class OrderItemDto {
    private String id;
    private String orderId;
    private String productId;
    private Integer quantity;
    private Double unitPrice;
    private Double totalPrice;
    private LocalDateTime creationTimestamp;
    private LocalDateTime updateTimestamp;
}
