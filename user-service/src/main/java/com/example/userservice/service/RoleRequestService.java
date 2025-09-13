package com.example.userservice.service;

import com.example.userservice.enums.RequestStatus;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.*;
import com.example.userservice.repository.RoleRequestRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.repository.VetProfileRepository;
import com.example.userservice.repository.ShelterRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Slf4j
@Service
@RequiredArgsConstructor
public class RoleRequestService {
    
    private final RoleRequestRepository roleRequestRepository;
    private final UserRepository userRepository;
    private final VetProfileRepository vetProfileRepository;
    private final ShelterRepository shelterRepository;

    public RoleRequest getRoleRequestById(String requestId){
        return roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new NotFoundException("404"));
    }
    
    public RoleRequest createRoleRequest(String userId, Role requestedRole, String reason) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        
        // Kiểm tra xem đã có request pending chưa
        Optional<RoleRequest> existingRequest = roleRequestRepository
                .findByUserIdAndRequestedRoleAndStatus(userId, requestedRole, RequestStatus.PENDING);
        
        if (existingRequest.isPresent()) {
            throw new RuntimeException("You already have a pending request for this role");
        }
        
        // Kiểm tra xem user đã có role này chưa
        if (user.hasRole(requestedRole)) {
            throw new RuntimeException("You already have this role");
        }
        
        RoleRequest request = RoleRequest.builder()
                .user(user)
                .requestedRole(requestedRole)
                .reason(reason)
                .status(RequestStatus.PENDING)
                .build();
        
        return roleRequestRepository.save(request);
    }
    
    @Transactional
    public RoleRequest approveRequest(String requestId, String adminId, String adminNote) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }
        String userId = request.getUser().getId();
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));
        Role requestedRole = request.getRequestedRole();
        user.addRole(requestedRole);
        userRepository.save(user);
        if (requestedRole == Role.VET) {
            createVetProfile(userId);
        } else if (requestedRole == Role.SHELTER) {
            createShelterProfile(userId);
        }
        request.setStatus(RequestStatus.APPROVED);
        request.setReviewedBy(adminId);
        request.setReviewedAt(LocalDateTime.now());
        request.setAdminNote(adminNote);
        return roleRequestRepository.save(request);
    }
    
    @Transactional
    public RoleRequest rejectRequest(String requestId, String adminId, String rejectionReason) {
        RoleRequest request = roleRequestRepository.findById(requestId)
                .orElseThrow(() -> new RuntimeException("Request not found"));
        
        if (request.getStatus() != RequestStatus.PENDING) {
            throw new RuntimeException("Request is not pending");
        }
        
        request.setStatus(RequestStatus.REJECTED);
        request.setReviewedBy(adminId);
        request.setReviewedAt(LocalDateTime.now());
        request.setRejectionReason(rejectionReason);
        
        return roleRequestRepository.save(request);
    }
    
    public List<RoleRequest> getPendingRequests() {
        return roleRequestRepository.findByStatusOrderByCreationTimestampDesc(RequestStatus.PENDING);
    }
    
    public List<RoleRequest> getUserRequests(String userId) {
        return roleRequestRepository.findByUserIdOrderByCreationTimestampDesc(userId);
    }
    
    private void createVetProfile(String userId) {
        if (vetProfileRepository.existsById(userId)) return;
        User user = userRepository.getReferenceById(userId);
        VetProfile vetProfile = VetProfile.builder()
                .user(user)                 // QUAN TRỌNG
                .specialization("")
                .yearsExperience(0)
                .clinicAddress("")
                .bio("")
                .availableHoursJson("{}")
                .build();
        vetProfileRepository.saveAndFlush(vetProfile);
    }
    private void createShelterProfile(String userId) {
        if (shelterRepository.existsById(userId)) return;
        User user = userRepository.getReferenceById(userId);
        ShelterProfile shelterProfile = ShelterProfile.builder()
                .user(user)                 // QUAN TRỌNG
                .shelterName("")
                .contactEmail("")
                .hotline("")
                .address("")
                .description("")
                .verified(false)
                .build();
        shelterRepository.saveAndFlush(shelterProfile);
    }
}
