package com.example.userservice.model;

import com.example.userservice.enums.AdoptionStatus;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;

@Entity
@Table(name = "adoption_listings")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AdoptionListing extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id", nullable = false)
    private User shelter;
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "pet_id")
    private Pet pet; // có thể link thẳng tới pet thuộc shelter
    
    @Column(name = "pet_name", length = 120)
    private String petName; // fallback nếu không link pet
    
    @Column(length = 80)
    private String species;
    
    @Column(length = 120)
    private String breed;
    
    @Column(name = "age_months")
    private Integer ageMonths;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false, columnDefinition = "ENUM('AVAILABLE','PENDING','ADOPTED') DEFAULT 'AVAILABLE'")
    private AdoptionStatus status = AdoptionStatus.AVAILABLE;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt = LocalDateTime.now();
}

