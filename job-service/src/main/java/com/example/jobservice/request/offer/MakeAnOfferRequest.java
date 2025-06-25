package com.example.jobservice.request.offer;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MakeAnOfferRequest {
    @NotBlank(message = "User ID cannot be blank")
    private String userId;
    @NotBlank(message = "Advert Id cannot be blank")
    private String advertId;
    @NotBlank(message = "Offered Price cannot be blank")
    private int offeredPrice;
}
