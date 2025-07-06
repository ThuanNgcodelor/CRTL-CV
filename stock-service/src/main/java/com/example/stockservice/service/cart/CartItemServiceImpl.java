package com.example.stockservice.service.cart;

import com.example.stockservice.model.Cart;
import com.example.stockservice.model.CartItem;
import com.example.stockservice.model.Product;
import com.example.stockservice.repository.CartItemRepository;
import com.example.stockservice.repository.CartRepository;
import com.example.stockservice.request.cart.AddCartItemRequest;
import com.example.stockservice.request.cart.UpdateCartItemRequest;
import com.example.stockservice.service.product.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemServiceImpl implements CartItemService {
    private final CartService cartService;
    private final ProductService productService;
    private final CartItemRepository cartItemRepository;
    private final CartRepository cartRepository;

    @Override
    public CartItem addCartItem(AddCartItemRequest request) {
        Cart cart;
        if(request.getCartId() != null){
            cart = cartService.getCartById(request.getCartId());
        }else {
            cart = cartService.getCartByUserId(request.getUserId());
            if(cart == null) {
                cart = cartService.initializeCart(request.getUserId());
            }
            request.setCartId(cart.getId());
        }
        addToCart(request.getCartId(),  request.getProductId(), request.getQuantity());
        return cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(request.getProductId()))
                .findFirst().orElseThrow(()-> new RuntimeException("null cart item"));
    }

    @Override
    public void addToCart(String cartId, String productId, int quantity) {
        Cart cart = cartService.getCartById(cartId);
        Product product = productService.getProductById(productId);
        CartItem cartItem = cart.getItems().stream()
                .filter(item -> item.getProduct().getId().equals(product.getId()))
                  .findFirst()
                  .orElse(new CartItem());
        if(cartItem.getId() != null){
            cartItem.setQuantity(cartItem.getQuantity() + quantity);
            cartItem.setTotalPrice();
            cartItemRepository.save(cartItem);
        }else {
            cartItem.setCart(cart);
            cartItem.setProduct(product);
            cartItem.setQuantity(quantity);
            cartItem.setUnitPrice(product.getPrice());
            cartItem.setTotalPrice();
            cartItemRepository.save(cartItem);
            cart.getItems().add(cartItem);
        }
        cartRepository.save(cart);
    }

    @Override
    public CartItem updateCartItem(UpdateCartItemRequest request) {
        Cart cart = cartService.getCartByUserId(request.getUserId());
        if(cart == null)
            throw new RuntimeException("Cart not found for user: " + request.getUserId());
        CartItem updatedItem = cart.getItems().stream()
                .filter(c -> c.getProduct().getId().equals(request.getProductId()))
                .findFirst()
                .orElseThrow(() -> new RuntimeException("Product not found in cart: " + request.getProductId()));
        updatedItem.setQuantity(request.getQuantity());
        updatedItem.setUnitPrice(updatedItem.getProduct().getPrice());
        updatedItem.setTotalPrice();

        double totalAmount = cart.getItems().stream()
                .mapToDouble(CartItem::getTotalPrice)
                .sum();
        cart.setTotalAmount(totalAmount);
        cartRepository.save(cart);
        return updatedItem;
    }

    @Override
    public CartItem getCartItem(String cartId, String productId) {
        return cartItemRepository.findByCartIdAndProductId(cartId, productId);
    }

    @Override
    public void removeCartItem(String userId, String productId) {
        Cart cart = cartService.getCartByUserId(userId);
        if(cart == null)
            throw  new RuntimeException("Cart not found for user: ");
        CartItem item = getCartItem(cart.getId(), productId);
        cart.removeItem(item);
        cartItemRepository.delete(item);
    }
}
