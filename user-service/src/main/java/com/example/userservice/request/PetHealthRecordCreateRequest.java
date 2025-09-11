package com.example.userservice.request;

import com.example.userservice.enums.HealthEventType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class PetHealthRecordCreateRequest {
    private HealthEventType eventType; // VACCINATION, DEWORMING, CHECKUP, SURGERY, ILLNESS, OTHER
    private LocalDate eventDate;
    private String vetName;
    private String clinic;
    private String description;
    private String attachmentId;       // optional; set after upload
}