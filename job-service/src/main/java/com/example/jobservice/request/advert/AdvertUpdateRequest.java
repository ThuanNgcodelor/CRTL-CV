package com.example.jobservice.request.advert;

import com.example.jobservice.enums.Advertiser;
import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class AdvertUpdateRequest {
    @NotBlank(message = "Advert ID cannot be blank")
    private String id;
    @NotBlank(message = "Advert name cannot be blank")
    private String name;
    private String description;
    @NotBlank(message = "Delivery can not be blank")
    private int deliveryTime;
    @NotBlank(message = "Price cannot be blank")
    private int price;
    @NotBlank(message = "Advertiser cannot be blank")
    private Advertiser advertiser;
    private String userId;
    private String jobId;
}