package com.example.userservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyVetDto {
    private String userId;
    private String specialization;
    private Integer yearsExperience;
    private String clinicAddress;
    private String bio;
    private String availableHoursJson;
    private LocationDto clinicLocation;
    private Double distance; // km
    private String vetName;
    private String vetPhone;
    private String vetEmail;
}
