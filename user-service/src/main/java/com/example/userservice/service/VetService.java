package com.example.userservice.service;

import com.example.userservice.dto.VetProfileDto;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.VetProfile;
import com.example.userservice.repository.VetProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class VetService {
    private final VetProfileRepository vetRepo;
    private final ModelMapper mapper;

    public List<VetProfileDto> search(String province, String specialization, String q) {
        if (specialization != null && !specialization.isBlank()) {
            return vetRepo.findBySpecializationContainingIgnoreCase(specialization)
                    .stream().map(v -> mapper.map(v, VetProfileDto.class)).toList();
        }
        if (province != null && !province.isBlank()) {
            return vetRepo.findByClinicAddressContainingIgnoreCase(province)
                    .stream().map(v -> mapper.map(v, VetProfileDto.class)).toList();
        }
        if (q != null && !q.isBlank()) {
            return vetRepo.findByBioContainingIgnoreCase(q)
                    .stream().map(v -> mapper.map(v, VetProfileDto.class)).toList();
        }
        return vetRepo.findAll().stream().map(v -> mapper.map(v, VetProfileDto.class)).toList();
    }

    public VetProfileDto getById(String vetUserId) {
        VetProfile v = vetRepo.findById(vetUserId).orElseThrow(() -> new NotFoundException("Vet not found"));
        return mapper.map(v, VetProfileDto.class);
    }

    public List<VetProfile> getAll(){
        return vetRepo.findAll();
    }

    @Transactional
    public VetProfileDto updateMyProfile(String userId, VetProfileDto request) {
        VetProfile profile = vetRepo.findById(userId)
                .orElseThrow(() -> new NotFoundException("Vet not found"));

        if (request.getSpecialization() != null) profile.setSpecialization(request.getSpecialization());
        if (request.getYearsExperience() != null) profile.setYearsExperience(request.getYearsExperience());
        if (request.getClinicAddress() != null) profile.setClinicAddress(request.getClinicAddress());
        if (request.getBio() != null) profile.setBio(request.getBio());
        if (request.getAvailableHoursJson() != null) profile.setAvailableHoursJson(request.getAvailableHoursJson());

        VetProfile saved = vetRepo.save(profile);
        return mapper.map(saved, VetProfileDto.class);
    }
}
