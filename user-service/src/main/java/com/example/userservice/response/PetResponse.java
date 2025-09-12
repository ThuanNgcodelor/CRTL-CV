//package com.example.userservice.response;
//
//
//import com.example.userservice.enums.Gender;
//import com.example.userservice.enums.PetStatus;
//import lombok.Data;
//
//import java.math.BigDecimal;
//import java.time.LocalDate;
//
//@Data
//public class PetResponse {
//    private String id;
//    private String name;
//    private String species;
//    private String breed;
//    private LocalDate birthDate;
//    private Gender gender;
//    private String color;
//    private BigDecimal weightKg;
//    private String microchipNumber;
//    private Boolean vaccinated;
//    private Boolean sterilized;
//    private LocalDate lastVetVisit;
//    private PetStatus status;
//    private String notes;
//    private String primaryImageId;
//    private String createdAt;
//    private String updatedAt;
//}
package com.example.userservice.response;

import com.example.userservice.enums.Gender;
import com.example.userservice.enums.PetStatus;
import com.example.userservice.enums.AdoptionStatus;
import lombok.Data;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
public class PetResponse {
    private String id;
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

    private PetStatus status;

    private AdoptionStatus adoptionStatus;

    private String notes;
    private String primaryImageId;

    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    private String adoptionRequestJson;
    private LocalDateTime adoptionRequestedAt;
    private LocalDateTime adoptionReviewedAt;
    private String adoptionRequestedBy;
    private String adoptionNote;
}