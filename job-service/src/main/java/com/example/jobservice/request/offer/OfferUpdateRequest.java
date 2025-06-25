package com.example.jobservice.request.offer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class OfferUpdateRequest {
    @NotBlank(message = "Offer ID cannot be blank")
    private String id;
    private int offeredPrice;
}
