package com.example.stockservice.controller;

import com.example.stockservice.dto.CartDto;
import com.example.stockservice.dto.CartItemDto;
import com.example.stockservice.jwt.JwtUtil;
import com.example.stockservice.model.Cart;
import com.example.stockservice.model.CartItem;
import com.example.stockservice.request.cart.AddCartItemRequest;
import com.example.stockservice.request.cart.RemoveCartItemRequest;
import com.example.stockservice.request.cart.UpdateCartItemRequest;
import com.example.stockservice.service.cart.CartItemService;
import com.example.stockservice.service.cart.CartService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/stock/cart")
public class CartController {
    private final CartService cartService;
    private final CartItemService cartItemService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;


    @PostMapping("/item/add")
    ResponseEntity<CartItemDto> addToCart(@RequestBody AddCartItemRequest request){
        CartItem cartItem = cartItemService.addCartItem(request);
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
        return ResponseEntity.ok(cartItemDto);
    }

    @PatchMapping("/item/update")
    ResponseEntity<CartItemDto> updateCartItem(@RequestBody UpdateCartItemRequest request, HttpServletRequest httpRequest){
        String userId = jwtUtil.ExtractUserId(httpRequest);
        request.setUserId(userId);
        CartItem cartItem = cartItemService.updateCartItem(request);
        CartItemDto cartItemDto = modelMapper.map(cartItem, CartItemDto.class);
        return ResponseEntity.ok(cartItemDto);
    }

    @DeleteMapping("/item/remove/{productId}")
    ResponseEntity<Void> removeCartItem(@PathVariable String productId, HttpServletRequest httpRequest){
        String userId = jwtUtil.ExtractUserId(httpRequest);
        cartItemService.removeCartItem(userId, productId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/user")
    public ResponseEntity<CartDto> getCart(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        Cart cart = cartService.getCartByUserId(userId);
        CartDto cartDto = modelMapper.map(cart, CartDto.class);
        return ResponseEntity.ok(cartDto);
    }

}
