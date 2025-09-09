package com.example.userservice.repository;

import com.example.userservice.model.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;

public interface VetProfileRepository extends JpaRepository<VetProfile ,String> {
}
