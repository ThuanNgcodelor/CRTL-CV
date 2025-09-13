package com.example.userservice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthRecordDto {
    private String id;
    private String petId;
    private String petName;
    private String vetId;
    private String vetName;
    private LocalDateTime visitTime;
    private String diagnosis;
    private String treatment;
    private String notes;
    private List<HealthDocumentDto> documents;
    private String ownerId;
    private String ownerName;
}
