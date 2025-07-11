package com.example.orderservice.request;

import lombok.Data;

@Data
public class DecreaseStockRequest {
    private String productId;
    private int quantity;
}