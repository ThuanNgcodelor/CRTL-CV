package com.example.userservice.service;

import com.example.userservice.dto.VetProfileDto;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.VetProfile;
import com.example.userservice.repository.VetProfileRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;

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
}
