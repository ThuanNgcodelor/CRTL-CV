package com.example.jobservice.repository;

import com.example.jobservice.enums.Advertiser;
import com.example.jobservice.model.Advert;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface AdvertRepository extends JpaRepository<Advert,String> {
    List<Advert> getAdvertsByUserIdAndAdvertiser(String userId, Advertiser advertiser);
}
