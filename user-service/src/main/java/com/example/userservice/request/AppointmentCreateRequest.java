package com.example.userservice.request;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentCreateRequest {
    private String petId;
    private String vetId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String reason;
}