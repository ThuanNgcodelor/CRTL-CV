package com.example.userservice.repository;

import com.example.userservice.model.HealthRecord;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthRecordRepository extends JpaRepository<HealthRecord, String> {
    @EntityGraph(attributePaths = {"pet", "vet", "documents", "pet.owner", "vet.userDetails", "pet.owner.userDetails"})
    List<HealthRecord> findAllByPet_IdOrderByVisitTimeDesc(String petId);
    
    @EntityGraph(attributePaths = {"pet", "vet", "documents", "pet.owner", "vet.userDetails", "pet.owner.userDetails"})
    List<HealthRecord> findAllByVet_IdOrderByVisitTimeDesc(String vetId);
    
    @EntityGraph(attributePaths = {"pet", "vet", "documents", "pet.owner", "vet.userDetails", "pet.owner.userDetails"})
    List<HealthRecord> findAllByVet_IdAndPet_IdOrderByVisitTimeDesc(String vetId, String petId);
}