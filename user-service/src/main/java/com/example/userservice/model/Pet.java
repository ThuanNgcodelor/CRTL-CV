package com.example.userservice.model;

import com.example.userservice.enums.Gender;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

@Entity
@Table(name = "pets")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Pet extends BaseEntity {
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id", nullable = false)
    private User owner;
    
    @Column(nullable = false, length = 120)
    private String name;
    
    @Column(nullable = false, length = 80)
    private String species; // Chó, mèo,...
    
    @Column(length = 120)
    private String breed;
    
    @Column(name = "birth_date")
    private LocalDate birthDate;
    
    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "ENUM('MALE','FEMALE','UNKNOWN') DEFAULT 'UNKNOWN'")
    private Gender gender;
    
    @Column(columnDefinition = "TEXT")
    private String notes;
    
    @Column(name = "image_id")
    private String imageId;
}


