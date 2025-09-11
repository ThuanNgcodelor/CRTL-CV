package com.example.userservice.service;

import com.example.userservice.dto.AppointmentDto;
import com.example.userservice.enums.AppointmentStatus;
import com.example.userservice.enums.Role;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.Appointment;
import com.example.userservice.model.Pet;
import com.example.userservice.model.User;
import com.example.userservice.repository.AppointmentRepository;
import com.example.userservice.repository.PetRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.AppointmentCreateRequest;
import jakarta.ws.rs.BadRequestException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AppointmentService {
    private final AppointmentRepository apptRepo;
    private final UserRepository userRepo;
    private final PetRepository petRepo;
    private final ModelMapper mapper;

    private AppointmentDto toDto(Appointment a) {
        AppointmentDto dto = new AppointmentDto();
        dto.setId(a.getId());
        dto.setPetId(a.getPet() != null ? a.getPet().getId() : null);
        dto.setOwnerId(a.getOwner() != null ? a.getOwner().getId() : null);
        dto.setVetId(a.getVet() != null ? a.getVet().getId() : null);
        dto.setStartTime(a.getStartTime());
        dto.setEndTime(a.getEndTime());
        dto.setStatus(a.getStatus() != null ? a.getStatus().name() : null);
        dto.setReason(a.getReason());
        return dto;
    }

    public AppointmentDto create(String ownerId, AppointmentCreateRequest req) {
        User owner = userRepo.findById(ownerId).orElseThrow(() -> new NotFoundException("Owner not found"));
        User vet = userRepo.findById(req.getVetId()).orElseThrow(() -> new NotFoundException("Vet not found"));
        if (vet.getRoles() == null || !vet.getRoles().contains(Role.VET)) {
            throw new BadRequestException("Selected user is not a vet");
        }
        Pet pet = petRepo.findById(req.getPetId()).orElseThrow(() -> new NotFoundException("Pet not found"));

        LocalDateTime start = req.getStartTime();
        LocalDateTime end = req.getEndTime();
        if (start == null || end == null || !end.isAfter(start)) {
            throw new BadRequestException("Invalid time range");
        }
        if (apptRepo.existsVetOverlap(vet.getId(), start, end)) {
            throw new BadRequestException("Vet time slot is not available");
        }
        if (apptRepo.existsOwnerOverlap(owner.getId(), start, end)) {
            throw new BadRequestException("Owner already has an appointment in this time");
        }

        Appointment appt = Appointment.builder()
                .owner(owner).vet(vet).pet(pet)
                .startTime(start).endTime(end)
                .status(AppointmentStatus.PENDING)
                .reason(req.getReason())
                .build();
        appt = apptRepo.save(appt);
        return toDto(appt);
    }

    public List<AppointmentDto> getMyAppointments(String ownerId) {
        return apptRepo.findAllByOwner_IdOrderByStartTimeDesc(ownerId)
                .stream().map(this::toDto).toList();
    }

    public List<AppointmentDto> getVetAppointments(String vetId) {
        return apptRepo.findAllByVet_IdOrderByStartTimeDesc(vetId)
                .stream().map(this::toDto).toList();
    }

    public AppointmentDto updateStatus(String id, String jwtUserId, String status) {
        Appointment appt = apptRepo.findById(id).orElseThrow(() -> new NotFoundException("Appointment not found"));
        if (!appt.getVet().getId().equals(jwtUserId) && !appt.getOwner().getId().equals(jwtUserId)) {
            throw new BadRequestException("No permission to update this appointment");
        }
        appt.setStatus(AppointmentStatus.valueOf(status));
        return toDto(apptRepo.save(appt));
    }
}
