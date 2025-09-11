package com.example.orderservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.Map;

@Data
public class OrderStatisticsDto {
    private Long totalOrders;
    private Double totalRevenue;
    private Double averageOrderValue;
    private Map<String, Long> ordersByStatus;
    private Map<String, Long> ordersByMonth;
    private LocalDateTime periodStart;
    private LocalDateTime periodEnd;
}
