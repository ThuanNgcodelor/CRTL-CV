package com.example.userservice.dto;

import lombok.Data;
import java.time.LocalDateTime;
import java.util.List;

@Data
public class HealthRecordDto {
    private String id;
    private String petId;
    private String vetId;
    private LocalDateTime visitTime;
    private String diagnosis;
    private String treatment;
    private String notes;
    private List<HealthDocumentDto> documents;
}