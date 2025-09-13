package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "health_records")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class HealthRecord extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id", nullable = false)
    private Pet pet;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id")
    private User vet; // nullable nếu tự ghi chú
    
    @Column(name = "visit_time")
    private LocalDateTime visitTime;
    
    @Column(columnDefinition = "TEXT")
    private String diagnosis;
    
    @Column(columnDefinition = "TEXT")
    private String treatment;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @OneToMany(mappedBy = "healthRecord", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<HealthDocument> documents;
}
