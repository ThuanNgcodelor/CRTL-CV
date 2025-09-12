package com.example.userservice.controller;


import com.example.userservice.request.PetHealthRecordCreateRequest;
import com.example.userservice.request.PetStatusUpdateRequest;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.PetCreateRequest;
import com.example.userservice.request.PetUpdateRequest;
import com.example.userservice.response.HealthRecordResponse;
import com.example.userservice.response.PetResponse;
import com.example.userservice.service.PetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
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
    private final ModelMapper modelMapper;

    // -------- ADMIN PET CRUD (/v1/user/pet/admin...) --------

    // GET /v1/user/pet/admin
    @GetMapping("/admin")
    public ResponseEntity<List<PetResponse>> adminListAll() {
        return ResponseEntity.ok(petService.listAllPets());
    }

    // GET /v1/user/pet/admin/{petId}
    @GetMapping("/admin/{petId}")
    public ResponseEntity<PetResponse> adminGetOne(
            @PathVariable String petId
    ) {
        return ResponseEntity.ok(petService.getPetById(petId));
    }

    // PUT multipart /v1/user/pet/admin/{petId}
    @PutMapping(path = "/admin/{petId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> adminUpdateWithImage(
            @PathVariable String petId,
            @RequestPart("request") PetUpdateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        return ResponseEntity.ok(petService.adminUpdatePetWithImage(petId, req, image));
    }

    // PUT /v1/user/pet/admin/{petId}/status
    @PutMapping("/admin/{petId}/status")
    public ResponseEntity<PetResponse> adminUpdateStatus(
            @PathVariable String petId,
            @RequestBody PetStatusUpdateRequest req
    ) {
        return ResponseEntity.ok(petService.adminUpdateStatus(petId, req));
    }

    // DELETE /v1/user/pet/admin/{petId}?soft=true
    @DeleteMapping("/admin/{petId}")
    public ResponseEntity<Void> adminDelete(
            @PathVariable String petId,
            @RequestParam(name = "soft", defaultValue = "true") boolean soft
    ) {
        petService.adminDeletePet(petId, soft);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllPet")
    public ResponseEntity<PetResponse> getLLPet(){
        return ResponseEntity.ok(modelMapper.map(petService.getAll(),PetResponse.class));
    }
// admin


    @PostMapping
    public ResponseEntity<PetResponse> create(
            HttpServletRequest request,
            @RequestBody PetCreateRequest req
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.createPet(userId, req));
    }

    // {
    //     "name": "Milo",
    //     "species": "Dog",
    //     "breed": "Shiba Inu",
    //     "birthDate": "2021-06-15",
    //     "gender": "MALE",
    //     "color": "Red Sesame",
    //     "weightKg": 9.8,
    //     "microchipNumber": "MC-123456789",
    //     "vaccinated": true,
    //     "sterilized": false,
    //     "lastVetVisit": "2024-12-20",
    //     "notes": "Hiền, thân thiện với trẻ em",
    //     "status": "ACTIVE"
    //   }
    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<PetResponse> createWithImage(
            HttpServletRequest request,
            @RequestPart("request") PetCreateRequest req,
            @RequestPart(value = "file", required = false) MultipartFile image
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.createPetWithImage(userId, req, image));
    }

    //{{baseURL}}/v1/user/pet
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

    //    //POST /v1/user/pet/{petId}/health-records
    //    //{
    //    //  "eventType": "CHECKUP",
    //    //  "eventDate": "2025-02-01",
    //    //  "vetName": "Dr. Nguyen",
    //    //  "clinic": "Happy Paws Clinic",
    //    //  "description": "Khám tổng quát, sức khỏe tốt",
    //    //}
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

    //
    @GetMapping("/{petId}/health-records")
    public ResponseEntity<List<HealthRecordResponse>> listHealthRecords(
            HttpServletRequest request,
            @PathVariable String petId
    ) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(petService.listHealthRecords(userId, petId));
    }
}