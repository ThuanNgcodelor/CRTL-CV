package com.example.orderservice.client;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.request.DecreaseStockRequest;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@FeignClient(name = "stock-service", path = "/v1/stock")
public interface StockServiceClient {
    @PostMapping("/product/decreaseStock")
    ProductDto decreaseStock(@RequestBody DecreaseStockRequest request);

    @GetMapping("/cart/user")
    ResponseEntity<CartDto> getCart(@RequestHeader("Authorization") String token);

    @GetMapping("/cart/getCartByUserId")
    ResponseEntity<CartDto> getCartByUserId(HttpServletRequest request);

    @GetMapping("/product/getProductById/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id);

    @DeleteMapping("/cart/clear/{cartId}")
    ResponseEntity<Void> clearCartByCartId(@PathVariable String cartId);
}
