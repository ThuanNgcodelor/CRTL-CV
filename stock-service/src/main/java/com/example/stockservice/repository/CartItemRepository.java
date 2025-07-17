package com.example.stockservice.repository;

import com.example.stockservice.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, String> {
    CartItem findByCartIdAndProductId(String cartId, String productId);
    void deleteByCartIdAndProductIdIn(String id, List<String> productIds);
    void deleteAllByCartId(String id);
}
