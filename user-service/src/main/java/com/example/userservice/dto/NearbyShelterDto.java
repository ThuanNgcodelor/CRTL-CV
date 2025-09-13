package com.example.userservice.dto;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NearbyShelterDto {
    private String userId;
    private String shelterName;
    private String contactEmail;
    private String hotline;
    private String address;
    private String description;
    private Boolean verified;
    private LocationDto shelterLocation;
    private Double distance; // km
}
