package com.example.userservice.repository;

import com.example.userservice.model.HealthRecord;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, String> {
    List<HealthRecord> findAllByPet_IdOrderByVisitTimeDesc(String petId);
}