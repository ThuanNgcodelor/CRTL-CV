package com.example.userservice.request;

import lombok.Data;

@Data
public class HealthRecordCreateRequest {
    private String petId;
    private String visitTime; 
    private String diagnosis;
    private String treatment;
    private String notes;
}

