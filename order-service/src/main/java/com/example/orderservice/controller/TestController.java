package com.example.orderservice.controller;

import com.example.orderservice.client.StockServiceClient;
import com.example.orderservice.dto.ProductDto;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/order/test")
public class TestController {
    private final StockServiceClient stockServiceClient;

    @GetMapping("/getId/{id}")
    ResponseEntity<ProductDto> getProductById(@PathVariable String id) {
        ResponseEntity<ProductDto> response = stockServiceClient.getProductById(id);
        if (response.getStatusCode().is2xxSuccessful()) {
            return ResponseEntity.ok(response.getBody());
        } else {
            return ResponseEntity.status(response.getStatusCode()).build();
        }
    }
}
