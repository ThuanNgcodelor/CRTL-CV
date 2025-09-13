package com.example.userservice.repository;

import com.example.userservice.model.ShelterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ShelterRepository extends JpaRepository<ShelterProfile, String> {
    Optional<ShelterProfile> findByUserId(String userId);
}
