package com.example.orderservice.service;

import com.example.orderservice.model.Order;

import java.util.List;

public interface OrderService {
    Order placeOrder(String userId);
    Order getOrderById(String orderId);
    List<Order> getUserOrders(String userId);
}
