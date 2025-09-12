package com.example.userservice.controller;

import com.example.userservice.dto.VetProfileDto;
import com.example.userservice.service.VetService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import jakarta.servlet.http.HttpServletRequest;
import com.example.userservice.jwt.JwtUtil;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/vets")
public class VetController {
    private final VetService vetService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;

    @GetMapping("/search")
    public ResponseEntity<List<VetProfileDto>> search(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(vetService.search(province, specialization, q));
    }

    //{{baseURL}}/v1/user/vets/getAllVet
    @GetMapping("/getAllVet")
    public ResponseEntity<List<VetProfileDto>> getALl() {
        var result = vetService.getAll().stream()
                .map(src -> {
                    VetProfileDto dto = modelMapper.map(src, VetProfileDto.class);
                    dto.setUserId(src.getUserId());
                    return dto;
                })
                .toList();
        return ResponseEntity.ok(result);
    }

    //{{baseURL}}/v1/user/vets/5c2c835f-0304-4ae1-91e4-60207a22e9f1
    @GetMapping("/{vetUserId}")
    public ResponseEntity<VetProfileDto> get(@PathVariable String vetUserId) {
        return ResponseEntity.ok(vetService.getById(vetUserId));
    }

    // Current authenticated vet profile
    @GetMapping("/me")
    public ResponseEntity<VetProfileDto> getMyProfile(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        return ResponseEntity.ok(vetService.getById(userId));
    }

    // Update current authenticated vet profile
    @PutMapping("/me")
    public ResponseEntity<VetProfileDto> updateMyProfile(HttpServletRequest request,
                                                         @RequestBody VetProfileDto req) {
        String userId = jwtUtil.ExtractUserId(request);
        return ResponseEntity.ok(vetService.updateMyProfile(userId, req));
    }
}