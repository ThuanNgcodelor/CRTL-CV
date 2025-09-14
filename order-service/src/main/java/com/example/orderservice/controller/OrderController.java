package com.example.orderservice.controller;

import com.example.orderservice.dto.*;
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

    // CRUD order

    // GET /v1/order/getAll?status=PENDING
    // [
    //  {
    //    "id": "550e8400-e29b-41d4-a716-446655440000",
    //    "userId": "user123",
    //    "totalPrice": 150.50,
    //    "orderStatus": "PENDING",
    //    "notes": "Giao hàng vào buổi chiều",
    //    "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
    //    "paymentMethod": "CASH",
    //    "creationTimestamp": "2024-01-15T10:30:00",
    //    "updateTimestamp": "2024-01-15T10:30:00",
    //    "orderItems": [
    //      {
    //        "id": "item001",
    //        "orderId": "550e8400-e29b-41d4-a716-446655440000",
    //        "productId": "prod123",
    //        "quantity": 2,
    //        "unitPrice": 75.25,
    //        "totalPrice": 150.50,
    //        "creationTimestamp": "2024-01-15T10:30:00",
    //        "updateTimestamp": "2024-01-15T10:30:00"
    //      }
    //    ]
    //  }
    //]
    @GetMapping("/getAll")
    ResponseEntity<List<OrderDto>> getAllOrders(
            @RequestParam(required = false) String status) {
        List<Order> orders = orderService.getAllOrders(status);
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    ///v1/order/update/{id}
    /// PUT /v1/order/update/550e8400-e29b-41d4-a716-446655440000
    /// Content-Type: application/json
    /// Request
    /// {
    ///   "orderStatus": "PROCESSING",
    ///   "notes": "Đang xử lý đơn hàng",
    ///   "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
    ///   "paymentMethod": "CASH"
    /// }
    @PutMapping("/update/{id}")
    ResponseEntity<OrderDto> updateOrder(@PathVariable String id, @RequestBody UpdateOrderRequest request) {
        Order order = orderService.updateOrder(id, request);
        return ResponseEntity.ok(modelMapper.map(order, OrderDto.class));
    }

    //6. PUT /v1/order/update-status/{id}
    //PUT /v1/order/update-status/550e8400-e29b-41d4-a716-446655440000?status=SHIPPED
    @PutMapping("/update-status/{id}")
    ResponseEntity<OrderDto> updateOrderStatus(@PathVariable String id, @RequestParam String status) {
        Order order = orderService.updateOrderStatus(id, status);
        return ResponseEntity.ok(modelMapper.map(order, OrderDto.class));
    }

    //## 7. DELETE /v1/order/delete/{id}
    //### Request
    //DELETE /v1/order/delete/550e8400-e29b-41d4-a716-446655440000
    @DeleteMapping("/delete/{id}")
    ResponseEntity<String> deleteOrder(@PathVariable String id) {
        orderService.deleteOrder(id);
        return ResponseEntity.ok("Order deleted successfully");
    }

    // GET /v1/order/search?userId=user123&status=COMPLETED&startDate=2024-01-01&endDate=2024-12-31
    //[
    //  {
    //    "id": "550e8400-e29b-41d4-a716-446655440001",
    //    "userId": "user123",
    //    "totalPrice": 299.99,
    //    "orderStatus": "COMPLETED",
    //    "notes": "Đã giao hàng thành công",
    //    "shippingAddress": "456 Đường XYZ, Quận 2, TP.HCM",
    //    "paymentMethod": "CREDIT_CARD",
    //    "creationTimestamp": "2024-01-10T14:20:00",
    //    "updateTimestamp": "2024-01-12T16:45:00",
    //    "orderItems": [
    //      {
    //        "id": "item002",
    //        "orderId": "550e8400-e29b-41d4-a716-446655440001",
    //        "productId": "prod456",
    //        "quantity": 1,
    //        "unitPrice": 199.99,
    //        "totalPrice": 199.99,
    //        "creationTimestamp": "2024-01-10T14:20:00",
    //        "updateTimestamp": "2024-01-10T14:20:00"
    //      },
    //      {
    //        "id": "item003",
    //        "orderId": "550e8400-e29b-41d4-a716-446655440001",
    //        "productId": "prod789",
    //        "quantity": 1,
    //        "unitPrice": 100.00,
    //        "totalPrice": 100.00,
    //        "creationTimestamp": "2024-01-10T14:20:00",
    //        "updateTimestamp": "2024-01-10T14:20:00"
    //      }
    //    ]
    //  }
    //]
    @GetMapping("/search")
    ResponseEntity<List<OrderDto>> searchOrders(
            @RequestParam(required = false) String userId,
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        List<Order> orders = orderService.searchOrders(userId, status, startDate, endDate);
        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());
        return ResponseEntity.ok(orderDtos);
    }

    //## 8. GET /v1/order/statistics
    //### Request
    //GET /v1/order/statistics?startDate=2024-01-01&endDate=2024-12-31
    //### Response
    //```json
    //{
    //  "totalOrders": 150,
    //  "totalRevenue": 45000.50,
    //  "averageOrderValue": 300.00,
    //  "ordersByStatus": {
    //    "PENDING": 25,
    //    "PROCESSING": 30,
    //    "SHIPPED": 45,
    //    "DELIVERED": 40,
    //    "CANCELLED": 10
    //  },
    //  "ordersByMonth": {
    //    "1": 12,
    //    "2": 15,
    //    "3": 18,
    //    "4": 22,
    //    "5": 20,
    //    "6": 25,
    //    "7": 28,
    //    "8": 30,
    //    "9": 25,
    //    "10": 22,
    //    "11": 18,
    //    "12": 15
    //  },
    //  "periodStart": "2024-01-01T00:00:00",
    //  "periodEnd": "2024-12-31T23:59:59"
    //}
    @GetMapping("/statistics")
    ResponseEntity<OrderStatisticsDto> getOrderStatistics(
            @RequestParam(required = false) String startDate,
            @RequestParam(required = false) String endDate) {
        OrderStatisticsDto statistics = orderService.getOrderStatistics(startDate, endDate);
        return ResponseEntity.ok(statistics);
    }

    //## 3. GET /v1/order/getOrderById/{id}
    // {
    //  "id": "550e8400-e29b-41d4-a716-446655440000",
    //  "userId": "user123",
    //  "totalPrice": 150.50,
    //  "orderStatus": "PENDING",
    //  "notes": "Giao hàng vào buổi chiều",
    //  "shippingAddress": "123 Đường ABC, Quận 1, TP.HCM",
    //  "paymentMethod": "CASH",
    //  "creationTimestamp": "2024-01-15T10:30:00",
    //  "updateTimestamp": "2024-01-15T10:30:00",
    //  "orderItems": [
    //    {
    //      "id": "item001",
    //      "orderId": "550e8400-e29b-41d4-a716-446655440000",
    //      "productId": "prod123",
    //      "quantity": 2,
    //      "unitPrice": 75.25,
    //      "totalPrice": 150.50,
    //      "creationTimestamp": "2024-01-15T10:30:00",
    //      "updateTimestamp": "2024-01-15T10:30:00"
    //    }
    //  ]
    //}
    @GetMapping("/getOrderById/{id}")
    ResponseEntity<OrderDto> getOrderById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(orderService.getOrderById(id), OrderDto.class));
    }

    @GetMapping("/getOrderByUserId")
    public ResponseEntity<List<OrderDto>> getOrderByUserId(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        List<Order> orders = orderService.getUserOrders(userId);

        List<OrderDto> orderDtos = orders.stream()
                .map(order -> modelMapper.map(order, OrderDto.class))
                .collect(Collectors.toList());

        return ResponseEntity.ok(orderDtos);
    }


//    @PostMapping("/create")
//    ResponseEntity<String> createOrder(HttpServletRequest request) {
//        orderService.placeOrderKafka(request);
//        return ResponseEntity.status(HttpStatus.CREATED).body("Order has been sent to Kafka.");
//    }

    @PostMapping("/create-from-cart")
    ResponseEntity<OrderDto> createOrderFromCart(@RequestBody FrontendOrderRequest request, HttpServletRequest httpRequest) {
        String userId = jwtUtil.ExtractUserId(httpRequest);
        String token = httpRequest.getHeader("Authorization");
        Order order = orderService.createOrderFromCart(request, userId, token);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(order, OrderDto.class));
    }
    // Address endpoints
    @GetMapping("/addresses")
    ResponseEntity<List<AddressDto>> getUserAddresses(HttpServletRequest request) {
        List<AddressDto> addresses = orderService.getUserAddresses(request);
        return ResponseEntity.ok(addresses);
    }

    @GetMapping("/addresses/{addressId}")
    ResponseEntity<AddressDto> getAddressById(@PathVariable String addressId) {
        AddressDto address = orderService.getAddressById(addressId);
        return ResponseEntity.ok(address);
    }

//    @PostMapping("/create-direct")
//    ResponseEntity<OrderDto> createOrderDirect(@RequestBody CreateOrderRequest request, HttpServletRequest httpRequest) {
//        String userId = jwtUtil.ExtractUserId(httpRequest);
//        request.setUserId(userId);
//        Order order = orderService.createOrder(request);
//        return ResponseEntity.status(HttpStatus.CREATED)
//                .body(modelMapper.map(order, OrderDto.class));
//    }

}