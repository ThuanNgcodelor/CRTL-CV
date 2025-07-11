package com.example.stockservice.repository;

import com.example.stockservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    CartItem findByCartIdAndProductId(String cartId, String productId);
    void deleteByCartIdAndProductId(String cartId, String productId);
    void deleteAllByCartId(String id);
}
