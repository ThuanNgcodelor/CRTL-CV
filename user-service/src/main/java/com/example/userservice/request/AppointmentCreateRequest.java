package com.example.userservice.request;

import jakarta.validation.constraints.NotNull;
import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentCreateRequest {
    @NotNull
    private String petId;
    
    @NotNull
    private String vetId;
    
    @NotNull
    private LocalDateTime startTime;

    @NotNull
    private LocalDateTime endTime;
    
    private String reason;
}