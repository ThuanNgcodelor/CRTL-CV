package com.example.userservice.repository;

import com.example.userservice.model.PetHealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PetHealthRecordRepository extends JpaRepository<PetHealthRecord, String> {
    List<PetHealthRecord> findByPet_Id(String petId);
}