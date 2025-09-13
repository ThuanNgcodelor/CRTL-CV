package com.example.userservice.controller;

import com.example.userservice.dto.HealthRecordDto;
import com.example.userservice.dto.VetHealthRecordDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.HealthRecordCreateRequest;
import com.example.userservice.service.HealthRecordService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/health-records")
public class HealthRecordController {
    private final HealthRecordService service;
    private final JwtUtil jwt;

    // {
    //   "petId": "...",
    //   "visitTime": "2025-09-12T09:30:00",
    //   "diagnosis": "...",
    //   "treatment": "...",
    //   "notes": "..."
    // }
    @PostMapping
    public ResponseEntity<HealthRecordDto> create(@Valid @RequestBody HealthRecordCreateRequest req, HttpServletRequest request) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.create(userId, req));
    }

    // Multipart upload for a specific record
    @PostMapping(path = "/{recordId}/documents", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HealthRecordDto> uploadDocument(
            @PathVariable String recordId,
            @RequestParam("file") MultipartFile file,
            @RequestParam(value = "docType", required = false) String docType,
            HttpServletRequest request
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.uploadDocument(userId, recordId, file, docType));
    }

    // For vet to list records by petId
    @GetMapping("/pet/{petId}")
    public ResponseEntity<List<HealthRecordDto>> listByPet(HttpServletRequest request, @PathVariable String petId) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.listByPet(userId, petId));
    }
    
    // Debug endpoint to check all health records for a pet
    @GetMapping("/debug/pet/{petId}")
    public ResponseEntity<Map<String, Object>> debugPetHealthRecords(HttpServletRequest request, @PathVariable String petId) {
        String userId = jwt.ExtractUserId(request);
        List<HealthRecordDto> records = service.listByPet(userId, petId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("petId", petId);
        response.put("totalRecords", records.size());
        response.put("records", records);
        
        return ResponseEntity.ok(response);
    }
    
    // Get health record details with documents
    @GetMapping("/{recordId}")
    public ResponseEntity<HealthRecordDto> getHealthRecord(HttpServletRequest request, @PathVariable String recordId) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.getHealthRecordWithDocuments(recordId, userId));
    }
    
    // Delete health record
    @DeleteMapping("/{recordId}")
    public ResponseEntity<Void> deleteHealthRecord(HttpServletRequest request, @PathVariable String recordId) {
        String userId = jwt.ExtractUserId(request);
        service.deleteHealthRecord(recordId, userId);
        return ResponseEntity.ok().build();
    }
    
    // Delete medical document
    @DeleteMapping("/documents/{documentId}")
    public ResponseEntity<Void> deleteHealthDocument(HttpServletRequest request, @PathVariable String documentId) {
        String userId = jwt.ExtractUserId(request);
        service.deleteHealthDocument(documentId, userId);
        return ResponseEntity.ok().build();
    }
    
    // Download medical document
    @GetMapping("/documents/{documentId}/download")
    public ResponseEntity<byte[]> downloadHealthDocument(HttpServletRequest request, @PathVariable String documentId) {
        String userId = jwt.ExtractUserId(request);
        return service.downloadHealthDocument(documentId, userId);
    }
    
    // ========== VET ENDPOINTS ==========
    
    // Get all health records for vet
    @GetMapping("/vet/all")
    public ResponseEntity<List<VetHealthRecordDto>> getVetHealthRecords(HttpServletRequest request) {
        String vetId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.getVetHealthRecords(vetId));
    }
    
    // Get health records for specific pet by vet
    @GetMapping("/vet/pet/{petId}")
    public ResponseEntity<List<VetHealthRecordDto>> getPetHealthRecordsByVet(
            HttpServletRequest request, 
            @PathVariable String petId) {
        String vetId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.getPetHealthRecordsByVet(vetId, petId));
    }
}


