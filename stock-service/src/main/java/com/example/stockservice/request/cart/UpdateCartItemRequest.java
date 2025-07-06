package com.example.stockservice.request.cart;

import lombok.Data;

@Data
public class UpdateCartItemRequest {
    private String productId;
    private Integer quantity;
    private String userId;
}
