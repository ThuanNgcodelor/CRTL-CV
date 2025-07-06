package com.example.stockservice.request.cart;

import lombok.Data;

@Data
public class RemoveCartItemRequest {
    public String userId;
    public String productId;
}
