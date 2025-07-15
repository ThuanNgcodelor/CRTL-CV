package com.example.orderservice.controller;

import com.example.orderservice.client.StockServiceClient;
import com.example.orderservice.dto.CartDto;
import com.example.orderservice.dto.ProductDto;
import com.example.orderservice.jwt.JwtUtil;
import com.example.orderservice.request.DecreaseStockRequest;
import com.example.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order/test")
public class TestController {
    private final StockServiceClient stockServiceClient;
    private final OrderService orderService;
    private final JwtUtil jwtUtil;

    @GetMapping("/getId/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        ResponseEntity<ProductDto> response = stockServiceClient.getProductById(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }

//    @PostMapping("/decreaseStock")
//    public ResponseEntity<ProductDto> decreaseStock(@RequestBody DecreaseStockRequest request) {
//        request.setProductId(request.getProductId());
//        request.setQuantity(request.getQuantity());
//        ProductDto productDto = stockServiceClient.decreaseStock(request);
//        return ResponseEntity.ok(productDto);
//    }

    @GetMapping("/getCart")
    ResponseEntity<CartDto> getCart(HttpServletRequest request){
        CartDto cartDto = orderService.getCartByUserId(request);
        return ResponseEntity.ok(cartDto);
    }
}
