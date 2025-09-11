package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "vet_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class VetProfile {
    @Id
    @Column(name = "user_id")
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String specialization;
    private Integer yearsExperience;
    private String clinicAddress;

    @Column(columnDefinition = "TEXT")
    private String bio;

    @Column(columnDefinition = "json")
    private String availableHoursJson;

    // Sửa mapping này - sử dụng @JoinColumn thay vì mappedBy
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id")
    private List<HealthRecord> healthRecords;

    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "vet_id")
    private List<Appointment> appointments;
}
