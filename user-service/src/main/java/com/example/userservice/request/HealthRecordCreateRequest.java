package com.example.userservice.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class HealthRecordCreateRequest {
    @NotNull
    @NotBlank
    private String petId;
    
    private String visitTime; 
    
    private String diagnosis;
    
    private String treatment;
    
    private String notes;
}

