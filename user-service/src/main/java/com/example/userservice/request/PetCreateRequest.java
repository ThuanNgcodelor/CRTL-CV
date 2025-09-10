package com.example.userservice.request;


import com.example.userservice.enums.Gender;
import com.example.userservice.enums.PetStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;

@Data
public class PetCreateRequest {
    private String name;
    private String species;
    private String breed;
    private LocalDate birthDate;
    private Gender gender;
    private String color;
    private BigDecimal weightKg;
    private String microchipNumber;
    private Boolean vaccinated;
    private Boolean sterilized;
    private LocalDate lastVetVisit;
    private String notes;
    private String primaryImageId;
    private PetStatus status; // default ACTIVE if null
}