package com.example.orderservice.service;

import com.example.orderservice.dto.*;
import com.example.orderservice.model.Order;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

public interface OrderService {
    // Existing methods
    Order placeOrder(HttpServletRequest request);
    Order getOrderById(String orderId);
    List<Order> getUserOrders(String userId);
    CartDto getCartByUserId(HttpServletRequest request);
    void placeOrderKafka(HttpServletRequest request);
    void consumeCartAndCreateOrder(CartDto cartDto);
    
    // CRUD methods
    Order createOrder(CreateOrderRequest request);
    List<Order> getAllOrders(String status);
    Order updateOrder(String orderId, UpdateOrderRequest request);
    Order updateOrderStatus(String orderId, String status);
    void deleteOrder(String orderId);
    List<Order> searchOrders(String userId, String status, String startDate, String endDate);
    OrderStatisticsDto getOrderStatistics(String startDate, String endDate);
    
    // Address methods
    List<AddressDto> getUserAddresses(HttpServletRequest request);
    AddressDto getAddressById(String addressId);
    Order createOrderFromCart(FrontendOrderRequest request, String userId, String authHeader);
    // Frontend order creation
    Order createOrderFromCart(FrontendOrderRequest request, String userId);
}
