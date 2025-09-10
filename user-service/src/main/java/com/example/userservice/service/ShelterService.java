package com.example.userservice.service;

import com.example.userservice.repository.PetRepository;
import com.example.userservice.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@RequiredArgsConstructor
@Service
public class ShelterService {
    private final PetRepository petRepository;
    private final ShelterRepository shelterRepository;


}
