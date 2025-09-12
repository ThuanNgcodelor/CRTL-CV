package com.example.userservice.service;

import com.example.userservice.dto.AppointmentDto;
import com.example.userservice.dto.HealthRecordDto;
import com.example.userservice.enums.AppointmentStatus;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.client.FileStorageClient;
import com.example.userservice.enums.DocumentType;
import com.example.userservice.model.*;
import com.example.userservice.repository.*;
import com.example.userservice.request.AppointmentCreateRequest;
import com.example.userservice.request.HealthRecordCreateRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class HealthRecordService {
    private final HealthRecordRepository recordRepo;
    private final HealthDocumentRepository documentRepo;
    private final UserRepository userRepo;
    private final PetRepository petRepo;
    private final ModelMapper mapper;
    private final FileStorageClient fileStorageClient;

    public HealthRecordDto create(String userId, HealthRecordCreateRequest req) {
        Pet pet = petRepo.findById(req.getPetId()).orElseThrow(() -> new NotFoundException("Pet not found"));
        User vet = userRepo.findById(userId).orElse(null);

        HealthRecord record = HealthRecord.builder()
                .pet(pet)
                .vet(vet)
                .visitTime(req.getVisitTime() != null ? LocalDateTime.parse(req.getVisitTime()) : LocalDateTime.now())
                .diagnosis(req.getDiagnosis())
                .treatment(req.getTreatment())
                .notes(req.getNotes())
                .build();
        record = recordRepo.save(record);
        return mapper.map(record, HealthRecordDto.class);
    }

    public HealthRecordDto uploadDocument(String userId, String recordId, MultipartFile image, String docType) {
        HealthRecord record = recordRepo.findById(recordId).orElseThrow(() -> new NotFoundException("Record not found"));
        if (image == null || image.isEmpty()) {
            throw new BadRequestException("File is required");
        }
        // Upload to file-storage service via Feign and use returned URL/ID
        String url = fileStorageClient.uploadImageToFIleSystem(image).getBody();
        HealthDocument doc = HealthDocument.builder()
                .healthRecord(record)
                .fileUrl(url)
                .build();
        if (docType != null && !docType.isBlank()) {
            try {
                doc.setDocType(DocumentType.valueOf(docType));
            } catch (IllegalArgumentException ignored) {
                // leave default OTHER
            }
        }
        documentRepo.save(doc);
        return mapper.map(record, HealthRecordDto.class);
    }

    public List<HealthRecordDto> listByPet(String requesterId, String petId) {
        return recordRepo.findAllByPet_IdOrderByVisitTimeDesc(petId)
                .stream().map(r -> mapper.map(r, HealthRecordDto.class)).toList();
    }
}
