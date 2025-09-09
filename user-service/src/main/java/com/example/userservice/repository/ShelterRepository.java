package com.example.userservice.repository;

import com.example.userservice.model.ShelterProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShelterRepository extends JpaRepository<ShelterProfile, String> {
}
