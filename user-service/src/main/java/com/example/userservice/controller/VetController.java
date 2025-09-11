package com.example.userservice.controller;

import com.example.userservice.dto.VetProfileDto;
import com.example.userservice.service.VetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/vets")
public class VetController {
    private final VetService vetService;

    @GetMapping("/search")
    public ResponseEntity<List<VetProfileDto>> search(
            @RequestParam(required = false) String province,
            @RequestParam(required = false) String specialization,
            @RequestParam(required = false) String q
    ) {
        return ResponseEntity.ok(vetService.search(province, specialization, q));
    }

    @GetMapping("/{vetUserId}")
    public ResponseEntity<VetProfileDto> get(@PathVariable String vetUserId) {
        return ResponseEntity.ok(vetService.getById(vetUserId));
    }
}