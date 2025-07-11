package com.example.stockservice.request.product;

import lombok.Data;

@Data
public class DecreaseStockRequest {
    private String productId;
    private int quantity;
}