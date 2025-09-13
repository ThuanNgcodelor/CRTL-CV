package com.example.userservice.repository;

import com.example.userservice.model.HealthDocument;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface HealthDocumentRepository extends JpaRepository<HealthDocument, String> {
    List<HealthDocument> findAllByHealthRecord_Id(String recordId);
}