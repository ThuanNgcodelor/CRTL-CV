package com.example.userservice.request;

import lombok.*;

@Getter @Setter @NoArgsConstructor @AllArgsConstructor @Builder
public class AdoptionRequestDto {
    private String fullName;
    private String phone;
    private String address;
    private Integer householdSize;
    private Boolean hasOtherPets;
    private String incomeRange;
    private String note;
}