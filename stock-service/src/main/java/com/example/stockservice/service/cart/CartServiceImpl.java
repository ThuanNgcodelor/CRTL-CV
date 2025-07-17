package com.example.stockservice.service.cart;

import com.example.stockservice.client.UserServiceClient;
import com.example.stockservice.dto.UserDto;
import com.example.stockservice.model.Cart;
import com.example.stockservice.repository.CartItemRepository;
import com.example.stockservice.repository.CartRepository;
import jakarta.transaction.Transactional;
import jakarta.ws.rs.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicLong;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final AtomicLong cartIdGenerator = new AtomicLong(0);
    private final UserServiceClient userServiceClient;

    @Override
    public Cart getCartById(String cartId) {
        return cartRepository.findById(cartId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Cart not found with id: " + cartId));
    }

    @Override
    public Cart getCartByUserId(String userId) {
        return cartRepository.findByUserId(userId).orElse(null);
    }

    @Override
    public Cart initializeCart(String userId) {
        userId = getUserById(userId).getId();
        Optional<Cart> existingCart = cartRepository.findByUserId(userId);
        if (existingCart.isPresent()) {
            return existingCart.get();
        }
        Cart newCart = new Cart();
        newCart.setUserId(userId);
        newCart.setTotalAmount(0.0);
        return cartRepository.save(newCart);
    }

    @Override
    public void clearCart(String userId) {
        Cart cart = cartRepository.findByUserId(userId).orElse(null);
        assert cart != null;
        cart.getItems().clear();
        cart.updateTotalAmount();
    }

    @Transactional
    @Override
    public void clearCartByCartId(String cartId) {
        Cart cart = getCartById(cartId);
        cartItemRepository.deleteAllByCartId(cartId);
        cart.getItems().clear();
        cartRepository.deleteById(cartId);
    }

    @Transactional
    public void removeCartItems(String cartId, List<String> productIds) {
        cartItemRepository.deleteByCartIdAndProductIdIn(cartId, productIds);
    }

    @Transactional
    @Override
    public void removeCartItemsAndUpdateCart(String cartId, List<String> productIds) {
        removeCartItems(cartId, productIds);
        Cart cart = cartRepository.findById(cartId).orElse(null);
        if(cart == null)
            throw new NotFoundException("Cart not found with id: " + cartId);
        cart.getItems().removeIf(item -> productIds.contains(item.getProduct().getId()));
        cart.updateTotalAmount();
    }

    @Transactional
    @Override
    public Cart getUserCart(String userId, String cartId) {
        Cart cart = cartRepository.findByIdAndUserId(userId, cartId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId + " and cartId: " + cartId));
        double totalAmount = cart.getTotalAmount();
        cart.setTotalAmount(totalAmount);
        cart.getItems().size();
        return cartRepository.save(cart);
    }

    protected UserDto getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId).getBody())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }
}
