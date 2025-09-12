package com.example.userservice.controller;

import com.example.userservice.dto.HealthRecordDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.HealthRecordCreateRequest;
import com.example.userservice.service.HealthRecordService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

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
    public ResponseEntity<HealthRecordDto> create(@RequestBody HealthRecordCreateRequest req, HttpServletRequest request) {
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
    public ResponseEntity<?> listByPet(HttpServletRequest request, @PathVariable String petId) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.listByPet(userId, petId));
    }
}


