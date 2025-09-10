package com.example.userservice.controller;

import com.example.userservice.dto.PetStatusUpdateRequest;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.HealthRecordCreateRequest;
import com.example.userservice.request.PetCreateRequest;
import com.example.userservice.request.PetUpdateRequest;
import com.example.userservice.response.HealthRecordResponse;
import com.example.userservice.response.PetResponse;
import com.example.userservice.service.PetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import jakarta.servlet.http.HttpServletRequest;

import java.util.List;

@RestController()
@RequiredArgsConstructor
@RequestMapping("/v1/user/pet")
public class PetController {

    private final PetService petService;
    private final JwtUtil jwt;

    @PostMapping
    public ResponseEntity<PetResponse> create(
            HttpServletRequest request,
            @RequestBody PetCreateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.createPet(userId, req));
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> createWithImage(
            HttpServletRequest request,
            @RequestPart("data") PetCreateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.createPetWithImage(userId, req, image));
    }

    @GetMapping
    public ResponseEntity<List<PetResponse>> myPets(
            HttpServletRequest request
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.listMyPets(userId));
    }

    @GetMapping("/{petId}")
    public ResponseEntity<PetResponse> getOne(
            HttpServletRequest request,
            @PathVariable String petId
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.getMyPet(userId, petId));
    }

    @PatchMapping("/{petId}")
    public ResponseEntity<PetResponse> update(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody PetUpdateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateMyPet(userId, petId, req));
    }

    @PatchMapping(path = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> updateWithImage(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestPart("data") PetUpdateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateMyPetWithImage(userId, petId, req, image));
    }

    @PatchMapping("/{petId}/status")
    public ResponseEntity<PetResponse> updateStatus(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody PetStatusUpdateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateStatus(userId, petId, req));
    }

    @DeleteMapping("/{petId}")
    public ResponseEntity<Void> delete(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestParam(name = "soft", defaultValue = "true") boolean soft
    ) {
        String userId = jwt.ExtractUserId(request);
        petService.deleteMyPet(userId, petId, soft);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/{petId}/health-records")
    public ResponseEntity<HealthRecordResponse> addHealthRecord(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody HealthRecordCreateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.addHealthRecord(userId, petId, req));
    }

    @PostMapping(path = "/{petId}/health-records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HealthRecordResponse> addHealthRecordWithUpload(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestPart("data") HealthRecordCreateRequest req,
            @RequestPart(value = "image", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.addHealthRecordWithUpload(userId, petId, req, image));
    }

    @GetMapping("/{petId}/health-records")
    public ResponseEntity<List<HealthRecordResponse>> listHealthRecords(
            HttpServletRequest request,
            @PathVariable String petId
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.listHealthRecords(userId, petId));
    }
}