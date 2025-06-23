package com.example.jobservice.model;

import com.example.jobservice.enums.OfferStatus;
import jakarta.persistence.*;
import lombok.*;

@Entity(name = "offers")
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Offer extends BaseEntity{
    private String userId;
    private int offeredPrice;

    @Enumerated(EnumType.STRING)
    private OfferStatus status;

    @ManyToOne(fetch = FetchType.LAZY,optional = false)
    @JoinColumn(name = "advert_id")
    private Advert advert;
}
