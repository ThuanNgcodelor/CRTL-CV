package com.example.userservice.request;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class ShelterProfileUpdateRequest {
    @NotBlank(message = "Shelter name is required")
    private String shelterName;

    @Email(message = "Invalid email format")
    @NotBlank(message = "Contact email is required")
    private String contactEmail;

    private String hotline;
    private String address;
    private String description;
}
