package com.example.stockservice.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.*;

@Entity(name = "cart_items")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class CartItem extends BaseEntity {
    @ManyToOne
    @JoinColumn(name = "product_id", nullable = false)
    private Product product;

    private int quantity;
    private double unitPrice;
    private double totalPrice;

    @JsonIgnore
    @ManyToOne // <--- bỏ cascade ở đây
    @JoinColumn(name = "cart_id", nullable = false)
    private Cart cart;

    public void setTotalPrice() {
        this.totalPrice = unitPrice * quantity;
    }
}
