package com.example.orderservice.controller;

import com.example.orderservice.dto.OrderDto;
import com.example.orderservice.jwt.JwtUtil;
import com.example.orderservice.model.Order;
import com.example.orderservice.service.OrderService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/order")
@RequiredArgsConstructor
public class OrderController {
    private final OrderService orderService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @PostMapping("/create")
    ResponseEntity<String> createOrder(HttpServletRequest request) {
        orderService.placeOrderKafka(request);
        return ResponseEntity.status(HttpStatus.CREATED).body("Order has been sent to Kafka.");
    }

//    @PostMapping("/create")
//    ResponseEntity<OrderDto> createOrder(HttpServletRequest request) {
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(modelMapper.map(orderService.placeOrder(request), OrderDto.class));
//    }

    @GetMapping("/getOrderByUserId")
    public ResponseEntity<List<OrderDto>> getOrderByUserId(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        List<Order> orders = orderService.getUserOrders(userId);

        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }

    @GetMapping("/getOrderById/{id}")
    ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(orderService.getOrderById(id), OrderDto.class));
    }

}
