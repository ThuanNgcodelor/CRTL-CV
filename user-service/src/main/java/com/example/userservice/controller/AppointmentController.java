package com.example.userservice.controller;

import com.example.userservice.dto.AppointmentDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.AppointmentCreateRequest;
import com.example.userservice.request.AppointmentStatusUpdateRequest;
import com.example.userservice.service.AppointmentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.servlet.http.HttpServletRequest;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/appointments")
public class AppointmentController {
    private final AppointmentService service;
    private final JwtUtil jwt;

    @PostMapping
    public ResponseEntity<AppointmentDto> create(@RequestBody AppointmentCreateRequest req, HttpServletRequest request) {
        String ownerId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.create(ownerId, req));
    }

    @GetMapping("/my")
    public ResponseEntity<List<AppointmentDto>> myAppointments(HttpServletRequest request) {
        String ownerId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.getMyAppointments(ownerId));
    }

    @GetMapping("/vet")
    public ResponseEntity<List<AppointmentDto>> vetAppointments(HttpServletRequest request) {
        String vetId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.getVetAppointments(vetId));
    }

    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable String id,
                                                       @RequestBody AppointmentStatusUpdateRequest req,
                                                       HttpServletRequest request) {
        String jwtUserId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.updateStatus(id, jwtUserId, req.getStatus()));
    }
}