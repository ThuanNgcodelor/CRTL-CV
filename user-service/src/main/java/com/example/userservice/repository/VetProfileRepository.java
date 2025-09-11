package com.example.userservice.repository;

import com.example.userservice.model.VetProfile;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface VetProfileRepository extends JpaRepository<VetProfile, String> {
    List<VetProfile> findBySpecializationContainingIgnoreCase(String specialization);
    List<VetProfile> findByClinicAddressContainingIgnoreCase(String clinicAddress);
    List<VetProfile> findByBioContainingIgnoreCase(String q);
}