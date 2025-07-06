package com.example.stockservice.service.cart;

import com.example.stockservice.model.Cart;

public interface CartService {
    Cart getCartByUserId(String userId);
    Cart initializeCart(String userId);
    void clearCart(String userId);
    Cart getUserCart(String userId, String cartId);
    Cart getCartById(String cartId);
}
