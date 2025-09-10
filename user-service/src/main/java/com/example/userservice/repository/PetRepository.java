package com.example.userservice.repository;

import com.example.userservice.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PetRepository extends JpaRepository<Pet,String> {
    List<Pet> findByOwner_Id(String ownerId);
    Optional<Pet> findByIdAndOwner_Id(String id, String ownerId);
}