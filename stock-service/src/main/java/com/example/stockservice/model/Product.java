package com.example.stockservice.model;

import com.example.stockservice.emuns.ProductStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "products")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Product extends BaseEntity {
    private String name;
    private String description;
    private double price;
    private double originalPrice;
    private double discountPercent = 0;
    private int stock;
    @Enumerated(EnumType.STRING)
    private ProductStatus status;
    private String imageId;
    @ManyToOne(fetch = FetchType.LAZY, cascade = CascadeType.ALL)
    @JoinColumn(name = "category_id")
    private Category category;
}
