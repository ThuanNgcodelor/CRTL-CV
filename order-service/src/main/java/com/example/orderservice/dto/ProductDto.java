package com.example.orderservice.dto;


import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ProductDto {
    private String id;
    private String name;
    private double price;
    private double originalPrice;
    private double discountPercent;
    private int stock;
    private String status;
}