package com.example.userservice.dto;

import lombok.Data;

@Data
public class HealthRecordDto {
    private String id;
    private String petId;
    private String vetId;
    private String visitTime;
    private String diagnosis;
    private String treatment;
    private String notes;
}
