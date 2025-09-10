package com.example.userservice.dto;

import com.example.userservice.enums.PetStatus;
import lombok.Data;

@Data
public class PetStatusUpdateRequest {
    private PetStatus status;
}