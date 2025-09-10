package com.example.userservice.repository;

import com.example.userservice.model.Pet;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PetRepository extends JpaRepository<Pet,String> {
}
