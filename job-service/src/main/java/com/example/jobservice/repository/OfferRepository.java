package com.example.jobservice.repository;

import com.example.jobservice.model.Offer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OfferRepository extends JpaRepository<Offer, String> {
    List<Offer> getOffersByUserId(String userId);
    List<Offer> getOffersByAdvertId(String advertId);
}
