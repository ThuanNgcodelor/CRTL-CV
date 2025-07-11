package com.example.orderservice.client;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.request.DecreaseStockRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "stock-service", path = "/v1/stock")
public interface StockServiceClient {
    @GetMapping("/cart/user")
    ResponseEntity<CartDto> getCart(@RequestHeader("Authorization") String token);
    @GetMapping("/product/getProductById/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id);
    @PostMapping("/product/decreaseStock")
    ResponseEntity<?> decreaseStock(@RequestBody DecreaseStockRequest request);
    @DeleteMapping("/cart/clear/{cartId}")
    ResponseEntity<Void> clearCartByCartId(@PathVariable String cartId);
}
