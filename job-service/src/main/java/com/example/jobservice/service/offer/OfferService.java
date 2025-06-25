package com.example.jobservice.service.offer;

import com.example.jobservice.dto.UserDto;
import com.example.jobservice.model.Offer;
import com.example.jobservice.request.offer.MakeAnOfferRequest;
import com.example.jobservice.request.offer.OfferUpdateRequest;

import java.util.List;

public interface OfferService {
    Offer makeAnOffer(MakeAnOfferRequest request);
    Offer getOfferById(String id);
    List<Offer> getOffersByAdvertId(String advertId);
    List<Offer> getOffersByUserId(String id);
    UserDto getUserById(String userId);
    Offer updateOfferById(OfferUpdateRequest request);
    void deleteOfferById(String id);
    boolean authorizeCheck(String id, String principal);
    Offer findOfferById(String id);
}
