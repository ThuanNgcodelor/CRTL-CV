package com.example.userservice.request;

import com.example.userservice.enums.AdoptionStatus;
import lombok.Data;

@Data
public class AdoptionStatusUpdateRequest {
    private AdoptionStatus status; // AVAILABLE | PENDING | ADOPTED
}
