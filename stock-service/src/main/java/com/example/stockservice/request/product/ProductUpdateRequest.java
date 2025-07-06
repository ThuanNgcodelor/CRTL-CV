package com.example.stockservice.request.product;

import lombok.Data;

@Data
public class ProductUpdateRequest {
    private String id;
    private String name;
    private String description;
    private double price;
    private double originalPrice;
    private double discountPercent;
    private int stock;
    private String imageId;
    private String status;
    private String categoryId;
}
