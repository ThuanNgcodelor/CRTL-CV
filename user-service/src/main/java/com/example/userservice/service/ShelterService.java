package com.example.userservice.service;

import com.example.userservice.dto.ShelterProfileDTO;
import com.example.userservice.model.ShelterProfile;
import com.example.userservice.repository.ShelterRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.ShelterProfileUpdateRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.http.HttpStatus;
@RequiredArgsConstructor
@Service
public class ShelterService {
    private final ShelterRepository shelterRepository;
    private final UserRepository userRepository;

    public ShelterProfileDTO getShelterProfile(String userId) {
        var profile = shelterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter profile not found"));
        return ShelterProfileDTO.fromEntity(profile);
    }

    @Transactional
    public ShelterProfileDTO createShelterProfile(String userId) {
        var existing = shelterRepository.findByUserId(userId);
        if (existing.isPresent()) return ShelterProfileDTO.fromEntity(existing.get());

        var user = userRepository.findById(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "User not found"));

        var p = new ShelterProfile();
        p.setUser(user);
        return ShelterProfileDTO.fromEntity(shelterRepository.save(p));
    }

    @Transactional
    public ShelterProfileDTO updateShelterProfile(String userId, ShelterProfileUpdateRequest req) {
        var profile = shelterRepository.findByUserId(userId)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Shelter profile not found"));

        profile.setShelterName(req.getShelterName());
        profile.setContactEmail(req.getContactEmail());
        profile.setHotline(req.getHotline());
        profile.setAddress(req.getAddress());
        profile.setDescription(req.getDescription());

        return ShelterProfileDTO.fromEntity(shelterRepository.save(profile));
    }
}