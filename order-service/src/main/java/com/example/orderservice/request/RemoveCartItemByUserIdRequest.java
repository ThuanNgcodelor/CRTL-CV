package com.example.orderservice.request;

import lombok.Data;
import java.util.List;

@Data
public class RemoveCartItemByUserIdRequest {
    private String userId;
    private List<String> productIds;
}
