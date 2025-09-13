package com.example.userservice.dto;

import lombok.Data;

import java.util.Set;

@Data
public class CartDto {
    private double totalAmount;
    private Set<CartItemDto> items;
}
