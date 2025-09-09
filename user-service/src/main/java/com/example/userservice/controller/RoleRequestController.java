package com.example.userservice.controller;

import com.example.userservice.enums.Role;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.model.RoleRequest;
import com.example.userservice.request.RoleRequestResponse;
import com.example.userservice.service.RoleRequestService;
import com.example.userservice.request.RoleRequestRequest;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/v1/user/role-requests")
@RequiredArgsConstructor
public class RoleRequestController {
    
    private final RoleRequestService roleRequestService;
    private final JwtUtil jwtUtil;

    @PostMapping
    public ResponseEntity<RoleRequestResponse> createRoleRequest(
            @RequestBody RoleRequestRequest request,
            HttpServletRequest httpRequest) {

        String userId = jwtUtil.ExtractUserId(httpRequest);
        RoleRequest created = roleRequestService.createRoleRequest(
                userId,
                Role.valueOf(request.getRole().toUpperCase()),
                request.getReason()
        );

        RoleRequestResponse response = RoleRequestResponse.builder()
                .id(created.getId())
                .userId(created.getUser().getId())
                .requestedRole(created.getRequestedRole().name())
                .reason(created.getReason())
                .status(created.getStatus().name())
                .build();

        return ResponseEntity.ok(response);
    }


    @GetMapping("/pending")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<RoleRequest>> getPendingRequests() {
        List<RoleRequest> requests = roleRequestService.getPendingRequests();
        return ResponseEntity.ok(requests);
    }
    
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<RoleRequest>> getUserRequests(@PathVariable String userId) {
        List<RoleRequest> requests = roleRequestService.getUserRequests(userId);
        return ResponseEntity.ok(requests);
    }
    
    @PostMapping("/{requestId}/approve")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleRequest> approveRequest(
            @PathVariable String requestId,
            @RequestParam String adminId,
            @RequestParam(required = false) String adminNote) {
        
        RoleRequest request = roleRequestService.approveRequest(requestId, adminId, adminNote);
        return ResponseEntity.ok(request);
    }
    
    @PostMapping("/{requestId}/reject")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<RoleRequest> rejectRequest(
            @PathVariable String requestId,
            @RequestParam String adminId,
            @RequestParam String rejectionReason) {
        
        RoleRequest request = roleRequestService.rejectRequest(requestId, adminId, rejectionReason);
        return ResponseEntity.ok(request);
    }
}
