package com.example.jobservice.dto;

import com.example.jobservice.enums.OfferStatus;
import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OfferDto {
    private String id;
    private String userId;
    private String advertId;
    private int offeredPrice;
    private OfferStatus offerStatus;
}
