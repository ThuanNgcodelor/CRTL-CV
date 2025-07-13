package com.example.stockservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CartDto {
    private String id;
    private String userId;
    private double totalAmount;
    private Set<CartItemDto> items;
}
