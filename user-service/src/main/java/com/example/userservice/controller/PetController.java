package com.example.userservice.controller;

import com.example.userservice.enums.AdoptionStatus;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.AdoptionRequestDto;
import com.example.userservice.request.AdoptionReviewRequest;
import com.example.userservice.request.AdoptionStatusUpdateRequest;
import com.example.userservice.request.PetCreateRequest;
import com.example.userservice.request.PetHealthRecordCreateRequest;
import com.example.userservice.request.PetStatusUpdateRequest;
import com.example.userservice.request.PetUpdateRequest;
import com.example.userservice.response.HealthRecordResponse;
import com.example.userservice.response.PetResponse;
import com.example.userservice.service.PetService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/pet")
public class PetController {
    private final PetService petService;
    private final JwtUtil jwt;
    private final ModelMapper modelMapper;


    @GetMapping("/admin")
    public ResponseEntity<List<PetResponse>> adminListAll() {
        return ResponseEntity.ok(petService.listAllPets());
    }

    @GetMapping("/admin/{petId}")
    public ResponseEntity<PetResponse> adminGetOne(@PathVariable String petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    @PutMapping(path = "/admin/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> adminUpdateWithImage(
            @PathVariable String petId,
            @RequestPart("request") PetUpdateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(petService.adminUpdatePetWithImage(petId, req, image));
    }

    @PutMapping("/admin/{petId}/status")
    public ResponseEntity<PetResponse> adminUpdateStatus(
            @PathVariable String petId,
            @RequestBody PetStatusUpdateRequest req
    ) {
        return ResponseEntity.ok(petService.adminUpdateStatus(petId, req));
    }

    @DeleteMapping("/admin/{petId}")
    public ResponseEntity<Void> adminDelete(
            @PathVariable String petId,
            @RequestParam(name = "soft", defaultValue = "true") boolean soft
    ) {
        petService.adminDeletePet(petId, soft);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllPet")
    public ResponseEntity<List<PetResponse>> getLLPet() {
        return ResponseEntity.ok(petService.listAllPets());
    }

    // ========================= USER (SHELTER/OWNER) CRUD =========================

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
            @RequestPart("request") PetCreateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.createPetWithImage(userId, req, image));
    }

    @GetMapping("/my")
    public ResponseEntity<List<PetResponse>> myPets(HttpServletRequest request) {
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

    @PutMapping("/{petId}")
    public ResponseEntity<PetResponse> update(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody PetUpdateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateMyPet(userId, petId, req));
    }

    @PutMapping(path = "/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> updateWithImage(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestPart("request") PetUpdateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateMyPetWithImage(userId, petId, req, image));
    }

    @PutMapping("/{petId}/status")
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
            @RequestBody PetHealthRecordCreateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.addHealthRecord(userId, petId, req));
    }

    @PostMapping(path = "/{petId}/health-records", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<HealthRecordResponse> addHealthRecordWithUpload(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestPart("request") PetHealthRecordCreateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
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


    @GetMapping("/public")
    public ResponseEntity<List<PetResponse>> listPublic(
            @RequestParam(value = "status", required = false) AdoptionStatus status
    ) {
        return ResponseEntity.ok(petService.listPublic(status));
    }

    @GetMapping("/public/{petId}")
    public ResponseEntity<PetResponse> publicGetOne(@PathVariable String petId) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }


    @PostMapping("/{petId}/adoption-request")
    public ResponseEntity<PetResponse> requestAdoption(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody AdoptionRequestDto body
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.requestAdoption(userId, petId, body));
    }

    @PostMapping("/{petId}/adoption/review")
    public ResponseEntity<PetResponse> reviewAdoption(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody AdoptionReviewRequest body
    ) {
        String shelterId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.reviewAdoption(shelterId, petId, body));
    }

    @PutMapping("/{petId}/adoption-status")
    public ResponseEntity<PetResponse> updateAdoptionStatus(
            HttpServletRequest request,
            @PathVariable String petId,
            @RequestBody AdoptionStatusUpdateRequest req
    ) {
        String shelterId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.updateAdoptionStatus(shelterId, petId, req));
    }
}
