package com.example.userservice.request;


import com.example.userservice.enums.HealthEventType;
import lombok.Data;

import java.time.LocalDate;

@Data
public class HealthRecordCreateRequest {
    private HealthEventType eventType;
    private LocalDate eventDate;
    private String vetName;
    private String clinic;
    private String description;
    private String attachmentId; // file-storage id
}