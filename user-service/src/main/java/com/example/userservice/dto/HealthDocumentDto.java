package com.example.userservice.dto;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class HealthDocumentDto {
    private String id;
    private String recordId;     // HealthDocument.healthRecord.id
    private String fileUrl;      // URL trả về từ file-storage
    private String docType;      // XRAY | LAB | CERTIFICATE | OTHER
    private LocalDateTime uploadedAt;
}