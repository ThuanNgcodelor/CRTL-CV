package com.example.userservice.request;


import lombok.Data;

@Data
public class AppointmentStatusUpdateRequest {
    private String status; // PENDING | CONFIRMED | RESCHEDULED | CANCELLED | DONE
}