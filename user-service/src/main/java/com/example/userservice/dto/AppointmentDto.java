package com.example.userservice.dto;


import lombok.Data;
import java.time.LocalDateTime;

@Data
public class AppointmentDto {
    private String id;
    private String petId;
    private String ownerId;
    private String vetId;
    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private String status;
    private String reason;
}