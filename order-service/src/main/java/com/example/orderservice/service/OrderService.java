package com.example.orderservice.service;

import com.example.orderservice.dto.CartDto;
import com.example.orderservice.model.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {
    Order placeOrder(HttpServletRequest request);
    Order getOrderById(String orderId);
    List<Order> getUserOrders(String userId);
    CartDto getCartByUserId(HttpServletRequest request);
}
