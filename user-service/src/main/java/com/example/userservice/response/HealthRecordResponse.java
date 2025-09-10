package com.example.userservice.response;

import com.example.userservice.enums.HealthEventType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthRecordResponse {
    private String id;
    private String petId;
    private HealthEventType eventType;
    private LocalDate eventDate;
    private String vetName;
    private String clinic;
    private String description;
    private String attachmentId;
    private String createdAt;
    private String updatedAt;
}