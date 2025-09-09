package com.example.userservice.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Table(name = "shelter_profiles")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ShelterProfile {
    @Id
    @Column(name = "user_id")
    private String userId;

    @OneToOne(fetch = FetchType.LAZY)
    @MapsId
    @JoinColumn(name = "user_id")
    private User user;

    private String shelterName;
    private String contactEmail;
    private String hotline;
    private String address;
    
    @Column(columnDefinition = "TEXT")
    private String description;

    private Boolean verified = false;

    // Sử dụng @JoinColumn thay vì mappedBy
    @OneToMany(cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    @JoinColumn(name = "shelter_id")
    private List<AdoptionListing> adoptionListings;
}
