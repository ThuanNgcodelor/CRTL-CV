package com.example.userservice.service;

import com.example.userservice.dto.HealthDocumentDto;
import com.example.userservice.dto.HealthRecordDto;
import com.example.userservice.dto.VetHealthRecordDto;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.client.FileStorageClient;
import com.example.userservice.enums.DocumentType;
import com.example.userservice.model.*;
import com.example.userservice.repository.*;
import com.example.userservice.request.HealthRecordCreateRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

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
        String url = fileStorageClient.uploadImageToFIleSystem(image).getBody();
        HealthDocument doc = HealthDocument.builder()
                .healthRecord(record)
                .fileUrl(url)
                .build();
        if (docType != null && !docType.isBlank()) {
            try {
                doc.setDocType(DocumentType.valueOf(docType));
            } catch (IllegalArgumentException ignored) {
            }
        }
        documentRepo.save(doc);
        return mapper.map(record, HealthRecordDto.class);
    }

    public List<HealthRecordDto> listByPet(String requesterId, String petId) {
        Pet pet = petRepo.findById(petId).orElseThrow(() -> new NotFoundException("Pet not found"));
        if (!pet.getOwner().getId().equals(requesterId)) {
            throw new BadRequestException("Access denied to this pet's health records");
        }
        
        return recordRepo.findAllByPet_IdOrderByVisitTimeDesc(petId)
                .stream().map(this::convertToHealthRecordDto).toList();
    }
    
    public List<VetHealthRecordDto> getVetHealthRecords(String vetId) {
        return recordRepo.findAllByVet_IdOrderByVisitTimeDesc(vetId)
                .stream().map(this::convertToVetHealthRecordDto).toList();
    }
    
    public List<VetHealthRecordDto> getPetHealthRecordsByVet(String vetId, String petId) {
        return recordRepo.findAllByVet_IdAndPet_IdOrderByVisitTimeDesc(vetId, petId)
                .stream().map(this::convertToVetHealthRecordDto).toList();
    }
    
    public HealthRecordDto getHealthRecordWithDocuments(String recordId, String requesterId) {
        HealthRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Health record not found"));
        
        boolean hasAccess = record.getPet().getOwner().getId().equals(requesterId) ||
                           (record.getVet() != null && record.getVet().getId().equals(requesterId));
        
        if (!hasAccess) {
            throw new BadRequestException("Access denied to this health record");
        }
        
        return convertToHealthRecordDto(record);
    }
    
    public void deleteHealthRecord(String recordId, String requesterId) {
        HealthRecord record = recordRepo.findById(recordId)
                .orElseThrow(() -> new NotFoundException("Health record not found"));
        
        boolean hasAccess = record.getPet().getOwner().getId().equals(requesterId) ||
                           (record.getVet() != null && record.getVet().getId().equals(requesterId));
        
        if (!hasAccess) {
            throw new BadRequestException("Access denied to delete this health record");
        }
        
        recordRepo.delete(record);
    }
    
    public void deleteHealthDocument(String documentId, String requesterId) {
        HealthDocument document = documentRepo.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document not found"));
        
        boolean hasAccess = document.getHealthRecord().getPet().getOwner().getId().equals(requesterId) ||
                           (document.getHealthRecord().getVet() != null && 
                            document.getHealthRecord().getVet().getId().equals(requesterId));
        
        if (!hasAccess) {
            throw new BadRequestException("Access denied to delete this document");
        }
        
        documentRepo.delete(document);
    }
    
    private HealthRecordDto convertToHealthRecordDto(HealthRecord record) {
        List<HealthDocumentDto> documents = record.getDocuments() != null ?
                record.getDocuments().stream()
                        .map(this::convertToHealthDocumentDto)
                        .collect(Collectors.toList()) : List.of();
        
        String vetName = "Unknown";
        if (record.getVet() != null && record.getVet().getUserDetails() != null) {
            String firstName = record.getVet().getUserDetails().getFirstName() != null ? 
                record.getVet().getUserDetails().getFirstName() : "";
            String lastName = record.getVet().getUserDetails().getLastName() != null ? 
                record.getVet().getUserDetails().getLastName() : "";
            vetName = (firstName + " " + lastName).trim();
            if (vetName.isEmpty()) vetName = "Unknown";
        }
        
        String ownerName = "Unknown";
        if (record.getPet().getOwner() != null && record.getPet().getOwner().getUserDetails() != null) {
            String firstName = record.getPet().getOwner().getUserDetails().getFirstName() != null ? 
                record.getPet().getOwner().getUserDetails().getFirstName() : "";
            String lastName = record.getPet().getOwner().getUserDetails().getLastName() != null ? 
                record.getPet().getOwner().getUserDetails().getLastName() : "";
            ownerName = (firstName + " " + lastName).trim();
            if (ownerName.isEmpty()) ownerName = "Unknown";
        }
        
        return HealthRecordDto.builder()
                .id(record.getId())
                .petId(record.getPet().getId())
                .petName(record.getPet().getName())
                .vetId(record.getVet() != null ? record.getVet().getId() : null)
                .vetName(vetName)
                .visitTime(record.getVisitTime())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .notes(record.getNotes())
                .documents(documents)
                .ownerId(record.getPet().getOwner().getId())
                .ownerName(ownerName)
                .build();
    }
    
    private VetHealthRecordDto convertToVetHealthRecordDto(HealthRecord record) {
        List<HealthDocumentDto> documents = record.getDocuments() != null ?
                record.getDocuments().stream()
                        .map(this::convertToHealthDocumentDto)
                        .collect(Collectors.toList()) : List.of();
        
        String ownerName = "Unknown";
        String ownerPhone = "N/A";
        String ownerEmail = "N/A";
        if (record.getPet().getOwner() != null && record.getPet().getOwner().getUserDetails() != null) {
            String firstName = record.getPet().getOwner().getUserDetails().getFirstName() != null ? 
                record.getPet().getOwner().getUserDetails().getFirstName() : "";
            String lastName = record.getPet().getOwner().getUserDetails().getLastName() != null ? 
                record.getPet().getOwner().getUserDetails().getLastName() : "";
            ownerName = (firstName + " " + lastName).trim();
            if (ownerName.isEmpty()) ownerName = "Unknown";
            
            ownerPhone = record.getPet().getOwner().getUserDetails().getPhoneNumber() != null ? 
                record.getPet().getOwner().getUserDetails().getPhoneNumber() : "N/A";
        }
        
        if (record.getPet().getOwner() != null) {
            ownerEmail = record.getPet().getOwner().getEmail() != null ? 
                record.getPet().getOwner().getEmail() : "N/A";
        }
        
        return VetHealthRecordDto.builder()
                .id(record.getId())
                .petId(record.getPet().getId())
                .petName(record.getPet().getName())
                .petSpecies(record.getPet().getSpecies())
                .petBreed(record.getPet().getBreed())
                .ownerId(record.getPet().getOwner().getId())
                .ownerName(ownerName)
                .ownerPhone(ownerPhone)
                .ownerEmail(ownerEmail)
                .visitTime(record.getVisitTime())
                .diagnosis(record.getDiagnosis())
                .treatment(record.getTreatment())
                .notes(record.getNotes())
                .documents(documents)
                .build();
    }
    
    // Download health document
    public ResponseEntity<byte[]> downloadHealthDocument(String documentId, String requesterId) {
        HealthDocument document = documentRepo.findById(documentId)
                .orElseThrow(() -> new NotFoundException("Document not found"));
        
        // Check if requester has access (owner or vet)
        boolean hasAccess = document.getHealthRecord().getPet().getOwner().getId().equals(requesterId) ||
                           (document.getHealthRecord().getVet() != null && 
                            document.getHealthRecord().getVet().getId().equals(requesterId));
        
        if (!hasAccess) {
            throw new BadRequestException("Access denied to this document");
        }
        
        // Extract file ID from URL (assuming URL format: /v1/file-storage/get/{fileId})
        String fileId = extractFileIdFromUrl(document.getFileUrl());
        if (fileId == null) {
            throw new BadRequestException("Invalid file URL");
        }
        
        try {
            return fileStorageClient.getImageById(fileId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file: " + e.getMessage());
        }
    }
    
    // Get file content from file-storage service
    public ResponseEntity<byte[]> getFileContent(String fileId) {
        try {
            return fileStorageClient.getImageById(fileId);
        } catch (Exception e) {
            throw new RuntimeException("Failed to retrieve file: " + e.getMessage());
        }
    }
    
    // Extract file ID from URL
    private String extractFileIdFromUrl(String fileUrl) {
        if (fileUrl == null || fileUrl.isEmpty()) {
            return null;
        }
        
        // Extract file ID from URL like: /v1/file-storage/get/{fileId} or similar
        String[] parts = fileUrl.split("/");
        if (parts.length > 0) {
            return parts[parts.length - 1];
        }
        return null;
    }
                    
    private HealthDocumentDto convertToHealthDocumentDto(HealthDocument document) {
        return HealthDocumentDto.builder()
                .id(document.getId())
                .fileUrl(document.getFileUrl())
                .docType(document.getDocType())
                .uploadedAt(document.getUploadedAt())
                .build();
    }
}
