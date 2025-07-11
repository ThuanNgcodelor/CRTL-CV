package com.example.orderservice.dto;

import lombok.Data;

@Data
public class OrderItemDto {
    private String id;
    private String orderId;
    private String productId;
    private int quantity;
    private  double price;
    private double unitPrice;
}
