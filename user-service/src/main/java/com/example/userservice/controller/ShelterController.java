package com.example.userservice.controller;

import com.example.userservice.dto.ShelterProfileDTO;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.ShelterProfileUpdateRequest;
import com.example.userservice.service.ShelterService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/shelter")
public class ShelterController {

    private final ShelterService shelterService;
    private final JwtUtil jwt;

    @PostMapping("/profile")
    public ResponseEntity<ShelterProfileDTO> createShelterProfile(HttpServletRequest request) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(shelterService.createShelterProfile(userId));
    }

    @GetMapping("/profile")
    public ResponseEntity<ShelterProfileDTO> getShelterProfile(HttpServletRequest request) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(shelterService.getShelterProfile(userId));
    }

    @PatchMapping("/profile")
    public ResponseEntity<ShelterProfileDTO> updateShelterProfile(
            HttpServletRequest request, @Valid @RequestBody ShelterProfileUpdateRequest req) {
        String userId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(shelterService.updateShelterProfile(userId, req));
    }
}
