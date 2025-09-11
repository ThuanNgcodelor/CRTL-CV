package com.example.orderservice.dto;

import lombok.Data;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import java.util.List;

@Data
public class CreateOrderRequest {
    @NotNull(message = "User ID is required")
    private String userId;
    
    @NotEmpty(message = "Order items cannot be empty")
    private List<CreateOrderItemRequest> orderItems;
    
    private String notes;
    private String shippingAddress;
    private String paymentMethod;
}
