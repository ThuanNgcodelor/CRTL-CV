package com.example.userservice.controller;

import com.example.userservice.dto.AppointmentDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.AppointmentCreateRequest;
import com.example.userservice.request.AppointmentStatusUpdateRequest;
import com.example.userservice.service.AppointmentService;
import jakarta.validation.Valid;
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

    //{
    //  "petId": "a59b392c-e62d-42a2-acab-8ca95b52c47a",
    //  "vetId": "5c2c835f-0304-4ae1-91e4-60207a22e9f1",
    //  "startTime": "2025-09-12T09:00:00",
    //  "endTime": "2025-09-12T10:00:00",
    //  "reason": "Khám sức khỏe định kỳ cho thú cưng"
    //}
    @PostMapping
    public ResponseEntity<AppointmentDto> create(@Valid @RequestBody AppointmentCreateRequest req, HttpServletRequest request) {
        String ownerId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.create(ownerId, req));
    }

    //{{baseURL}}/v1/user/appointments/my
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

    //{
    //  "status": "CONFIRMED"
    //}
    @PutMapping("/{id}/status")
    public ResponseEntity<AppointmentDto> updateStatus(@PathVariable String id,
                                                       @RequestBody AppointmentStatusUpdateRequest req,
                                                       HttpServletRequest request) {
        String jwtUserId = jwt.ExtractUserId(request);
        return ResponseEntity.ok(service.updateStatus(id, jwtUserId, req.getStatus()));
    }
}