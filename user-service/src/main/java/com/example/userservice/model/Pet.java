package com.example.userservice.model;

import com.example.userservice.enums.AdoptionStatus;
import com.example.userservice.enums.Gender;
import com.example.userservice.enums.PetStatus;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

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

    @Column(length = 60)
    private String color;

    @Column(name = "weight_kg", precision = 5, scale = 2)
    private BigDecimal weightKg;

    @Column(name = "microchip_number", length = 64, unique = false)
    private String microchipNumber;

    @Column(name = "vaccinated", nullable = false)
    private boolean vaccinated;

    @Column(name = "sterilized", nullable = false)
    private boolean sterilized;

    @Column(name = "last_vet_visit")
    private LocalDate lastVetVisit;

    @Enumerated(EnumType.STRING)
    @Column(name = "status", nullable = false, length = 24)
    private PetStatus status;

    @Column(columnDefinition = "TEXT")
    private String notes;

    @Column(name = "primary_image_id")
    private String primaryImageId;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetHealthRecord> healthRecords;

    @OneToMany(mappedBy = "pet", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<PetDocument> documents;

    @Enumerated(EnumType.STRING)
    @Column(name = "adoption_status", columnDefinition = "ENUM('AVAILABLE','PENDING','ADOPTED') DEFAULT 'AVAILABLE'")
    private AdoptionStatus adoptionStatus = AdoptionStatus.AVAILABLE;

    @Column(name = "adoption_requested_by")
    private String adoptionRequestedBy;

    @Column(name = "adoption_request_json", columnDefinition = "TEXT")
    private String adoptionRequestJson;

    @Column(name = "adoption_requested_at")
    private LocalDateTime adoptionRequestedAt;

    @Column(name = "adoption_reviewed_at")
    private LocalDateTime adoptionReviewedAt;

    @Column(name = "adoption_note")
    private String adoptionNote;
}