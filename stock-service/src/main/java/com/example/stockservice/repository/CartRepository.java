package com.example.stockservice.repository;

import com.example.stockservice.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CartRepository extends JpaRepository<Cart, String> {
    Optional<Cart> findByIdAndUserId(String userId, String cartId);

    Optional<Cart> findByUserId(String userId);
}
