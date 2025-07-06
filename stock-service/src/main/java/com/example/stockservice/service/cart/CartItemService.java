package com.example.stockservice.service.cart;

import com.example.stockservice.model.CartItem;
import com.example.stockservice.request.cart.AddCartItemRequest;
import com.example.stockservice.request.cart.UpdateCartItemRequest;

public interface CartItemService {
    void addToCart(String cartId, String productId, int quantity);
    CartItem updateCartItem(UpdateCartItemRequest request);
    CartItem getCartItem(String cartId, String productId);
    void removeCartItem(String userId, String productId);
    CartItem addCartItem(AddCartItemRequest request);
}
