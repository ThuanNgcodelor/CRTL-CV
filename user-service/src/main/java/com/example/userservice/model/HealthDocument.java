package com.example.userservice.model;

import com.example.userservice.enums.DocumentType;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "health_documents")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthDocument extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "record_id", nullable = false)
    private HealthRecord healthRecord;
    
    @Column(name = "file_url", nullable = false, length = 500)
    private String fileUrl;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "doc_type", columnDefinition = "ENUM('XRAY','LAB','CERTIFICATE','OTHER') DEFAULT 'OTHER'")
    @Builder.Default
    private DocumentType docType = DocumentType.OTHER;
    
    @Column(name = "uploaded_at", nullable = false)
    @Builder.Default
    private LocalDateTime uploadedAt = LocalDateTime.now();
}

