package com.example.userservice.dto;

import com.example.userservice.enums.DocumentType;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class HealthDocumentDto {
    private String id;
    private String fileUrl;
    private DocumentType docType;
    private LocalDateTime uploadedAt;
    private String fileName;
    private Long fileSize;
}