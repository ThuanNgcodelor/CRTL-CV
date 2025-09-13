package com.example.userservice.service;

import com.example.userservice.client.FileStorageClient;
import com.example.userservice.enums.AdoptionStatus;
import com.example.userservice.enums.HealthEventType;
import com.example.userservice.enums.PetStatus;
import com.example.userservice.model.Pet;
import com.example.userservice.model.PetHealthRecord;
import com.example.userservice.model.User;
import com.example.userservice.repository.PetHealthRecordRepository;
import com.example.userservice.repository.PetRepository;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.AdoptionStatusUpdateRequest;
import com.example.userservice.request.PetCreateRequest;
import com.example.userservice.request.PetHealthRecordCreateRequest;
import com.example.userservice.request.PetStatusUpdateRequest;
import com.example.userservice.request.PetUpdateRequest;
import com.example.userservice.response.HealthRecordResponse;
import com.example.userservice.response.PetResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PetService {
    private final ModelMapper modelMapper;
    private final PetRepository petRepository;
    private final UserRepository userRepository;
    private final PetHealthRecordRepository petHealthRecordRepository;
    private final FileStorageClient fileStorageClient;
    private final ObjectMapper json = new ObjectMapper();

    @Transactional
    public PetResponse createPet(String ownerUserId, PetCreateRequest req) {
        User owner = userRepository.findById(ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Owner not found"));

        Pet pet = Pet.builder()
                .owner(owner)
                .name(req.getName())
                .species(req.getSpecies())
                .breed(req.getBreed())
                .birthDate(req.getBirthDate())
                .gender(req.getGender())
                .color(req.getColor())
                .weightKg(req.getWeightKg())
                .microchipNumber(req.getMicrochipNumber())
                .vaccinated(Boolean.TRUE.equals(req.getVaccinated()))
                .sterilized(Boolean.TRUE.equals(req.getSterilized()))
                .lastVetVisit(req.getLastVetVisit())
                .status(req.getStatus() != null ? req.getStatus() : PetStatus.ACTIVE)
                .notes(req.getNotes())
                .primaryImageId(req.getPrimaryImageId())
                .build();

        pet = petRepository.save(pet);
        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse createPetWithImage(String ownerUserId, PetCreateRequest req, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String fileId = uploadGetId(image);
            req.setPrimaryImageId(fileId);
        }
        return createPet(ownerUserId, req);
    }

    public List<Pet> getAll() {
        return petRepository.findAll();
    }

    @Transactional(readOnly = true)
    public List<PetResponse> listMyPets(String ownerUserId) {
        return petRepository.findByOwner_Id(ownerUserId)
                .stream().map(this::toPetResponse).toList();
    }

    @Transactional(readOnly = true)
    public PetResponse getMyPet(String ownerUserId, String petId) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse updateMyPet(String ownerUserId, String petId, PetUpdateRequest req) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        if (req.getName() != null) pet.setName(req.getName());
        if (req.getSpecies() != null) pet.setSpecies(req.getSpecies());
        if (req.getBreed() != null) pet.setBreed(req.getBreed());
        if (req.getBirthDate() != null) pet.setBirthDate(req.getBirthDate());
        if (req.getGender() != null) pet.setGender(req.getGender());
        if (req.getColor() != null) pet.setColor(req.getColor());
        if (req.getWeightKg() != null) pet.setWeightKg(req.getWeightKg());
        if (req.getMicrochipNumber() != null) pet.setMicrochipNumber(req.getMicrochipNumber());
        if (req.getVaccinated() != null) pet.setVaccinated(req.getVaccinated());
        if (req.getSterilized() != null) pet.setSterilized(req.getSterilized());
        if (req.getLastVetVisit() != null) pet.setLastVetVisit(req.getLastVetVisit());
        if (req.getNotes() != null) pet.setNotes(req.getNotes());
        if (req.getPrimaryImageId() != null) pet.setPrimaryImageId(req.getPrimaryImageId());
        if (req.getStatus() != null) pet.setStatus(req.getStatus());

        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse updateMyPetWithImage(String ownerUserId, String petId, PetUpdateRequest req, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String fileId = uploadGetId(image);
            req.setPrimaryImageId(fileId);
        }
        return updateMyPet(ownerUserId, petId, req);
    }

    @Transactional
    public PetResponse updateStatus(String ownerUserId, String petId, PetStatusUpdateRequest req) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (req.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }
        pet.setStatus(req.getStatus());
        return toPetResponse(pet);
    }

    @Transactional
    public void deleteMyPet(String ownerUserId, String petId, boolean softDelete) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (softDelete) {
            pet.setStatus(PetStatus.INACTIVE);
        } else {
            petRepository.delete(pet);
        }
    }

    @Transactional
    public HealthRecordResponse addHealthRecord(String ownerUserId, String petId, PetHealthRecordCreateRequest req) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        if (req.getEventType() == null || req.getEventDate() == null) {
            throw new IllegalArgumentException("eventType and eventDate are required");
        }

        PetHealthRecord record = PetHealthRecord.builder()
                .pet(pet)
                .eventType(req.getEventType())
                .eventDate(req.getEventDate())
                .vetName(req.getVetName())
                .clinic(req.getClinic())
                .description(req.getDescription())
                .attachmentId(req.getAttachmentId())
                .build();

        record = petHealthRecordRepository.save(record);

        if (req.getEventType() == HealthEventType.CHECKUP || req.getEventType() == HealthEventType.SURGERY) {
            LocalDate current = pet.getLastVetVisit();
            if (current == null || req.getEventDate().isAfter(current)) {
                pet.setLastVetVisit(req.getEventDate());
                petRepository.save(pet);
            }
        }

        return toHealthRecordResponse(record);
    }

    @Transactional
    public HealthRecordResponse addHealthRecordWithUpload(String ownerUserId, String petId, PetHealthRecordCreateRequest req, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String fileId = uploadGetId(image);
            req.setAttachmentId(fileId);
        }
        return addHealthRecord(ownerUserId, petId, req);
    }

    @Transactional(readOnly = true)
    public List<HealthRecordResponse> listHealthRecords(String ownerUserId, String petId) {
        Pet pet = petRepository.findByIdAndOwner_Id(petId, ownerUserId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        return petHealthRecordRepository.findByPet_Id(pet.getId())
                .stream()
                .map(this::toHealthRecordResponse)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PetResponse> listAllPets() {
        return petRepository.findAll().stream().map(this::toPetResponse).toList();
    }

    @Transactional(readOnly = true)
    public PetResponse getPetById(String petId) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse adminCreatePet(String ownerUserId, PetCreateRequest req) {
        return createPet(ownerUserId, req);
    }

    @Transactional
    public PetResponse adminCreatePetWithImage(String ownerUserId, PetCreateRequest req, MultipartFile image) {
        return createPetWithImage(ownerUserId, req, image);
    }

    @Transactional
    public PetResponse adminUpdatePet(String petId, PetUpdateRequest req) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        if (req.getName() != null) pet.setName(req.getName());
        if (req.getSpecies() != null) pet.setSpecies(req.getSpecies());
        if (req.getBreed() != null) pet.setBreed(req.getBreed());
        if (req.getBirthDate() != null) pet.setBirthDate(req.getBirthDate());
        if (req.getGender() != null) pet.setGender(req.getGender());
        if (req.getColor() != null) pet.setColor(req.getColor());
        if (req.getWeightKg() != null) pet.setWeightKg(req.getWeightKg());
        if (req.getMicrochipNumber() != null) pet.setMicrochipNumber(req.getMicrochipNumber());
        if (req.getVaccinated() != null) pet.setVaccinated(req.getVaccinated());
        if (req.getSterilized() != null) pet.setSterilized(req.getSterilized());
        if (req.getLastVetVisit() != null) pet.setLastVetVisit(req.getLastVetVisit());
        if (req.getNotes() != null) pet.setNotes(req.getNotes());
        if (req.getPrimaryImageId() != null) pet.setPrimaryImageId(req.getPrimaryImageId());
        if (req.getStatus() != null) pet.setStatus(req.getStatus());

        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse adminUpdatePetWithImage(String petId, PetUpdateRequest req, MultipartFile image) {
        if (image != null && !image.isEmpty()) {
            String fileId = uploadGetId(image);
            req.setPrimaryImageId(fileId);
        }
        return adminUpdatePet(petId, req);
    }

    @Transactional
    public PetResponse adminUpdateStatus(String petId, PetStatusUpdateRequest req) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (req.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }
        pet.setStatus(req.getStatus());
        return toPetResponse(pet);
    }

    @Transactional
    public void adminDeletePet(String petId, boolean softDelete) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (softDelete) {
            pet.setStatus(PetStatus.INACTIVE);
        } else {
            petRepository.delete(pet);
        }
    }

    private String uploadGetId(MultipartFile file) {
        ResponseEntity<String> resp = fileStorageClient.uploadImageToFIleSystem(file);
        if (resp == null || !resp.getStatusCode().is2xxSuccessful() || resp.getBody() == null || resp.getBody().isBlank()) {
            throw new IllegalStateException("Upload file failed");
        }
        return resp.getBody();
    }

    private PetResponse toPetResponse(Pet pet) {
        return modelMapper.map(pet, PetResponse.class);
    }

    private HealthRecordResponse toHealthRecordResponse(PetHealthRecord record) {
        return modelMapper.map(record, HealthRecordResponse.class);
    }

    /* ===== Adoption features (đã có trong đoạn trên) ===== */

    @Transactional(readOnly = true)
    public List<PetResponse> listPublic(AdoptionStatus status) {
        AdoptionStatus s = (status != null) ? status : AdoptionStatus.AVAILABLE;
        return petRepository.findByAdoptionStatus(s).stream().map(this::toPetResponse).toList();
    }

    @Transactional
    public PetResponse requestAdoption(String userId, String petId, com.example.userservice.request.AdoptionRequestDto req) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        if (pet.getAdoptionStatus() != null && pet.getAdoptionStatus() != AdoptionStatus.AVAILABLE) {
            throw new IllegalStateException("Pet is not available for adoption");
        }
        try {
            pet.setAdoptionRequestedBy(userId);
            pet.setAdoptionRequestJson(json.writeValueAsString(req));
            pet.setAdoptionRequestedAt(LocalDateTime.now());
            pet.setAdoptionStatus(AdoptionStatus.PENDING);
            petRepository.save(pet);
            return toPetResponse(pet);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Transactional
    public PetResponse updateAdoptionStatus(String shelterId, String petId, AdoptionStatusUpdateRequest req) {
        if (req == null || req.getStatus() == null) {
            throw new IllegalArgumentException("status is required");
        }
        Pet pet = petRepository.findByIdAndOwner_Id(petId, shelterId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));

        // cập nhật trạng thái nhận nuôi
        pet.setAdoptionStatus(req.getStatus());

        // nếu mở lại AVAILABLE thì dọn dữ liệu yêu cầu cũ
        if (req.getStatus() == AdoptionStatus.AVAILABLE) {
            pet.setAdoptionRequestedBy(null);
            pet.setAdoptionRequestJson(null);
            pet.setAdoptionRequestedAt(null);
            pet.setAdoptionReviewedAt(null);
        }
        // ADOPTED: không chuyển owner ở đây (luồng approve đã làm)

        petRepository.save(pet);
        return toPetResponse(pet);
    }

    @Transactional
    public PetResponse reviewAdoption(String shelterId, String petId, com.example.userservice.request.AdoptionReviewRequest req) {
        Pet pet = petRepository.findById(petId)
                .orElseThrow(() -> new IllegalArgumentException("Pet not found"));
        // ensure shelter owns the pet originally (before adoption)
        if (pet.getOwner() == null || pet.getOwner().getId() == null || !pet.getOwner().getId().equals(shelterId)) {
            throw new IllegalStateException("Forbidden: not shelter owner");
        }
        if (pet.getAdoptionStatus() != AdoptionStatus.PENDING) {
            throw new IllegalStateException("No pending request");
        }
        pet.setAdoptionReviewedAt(LocalDateTime.now());
        pet.setNotes(req.getNote() != null ? req.getNote() : pet.getNotes());
        if (Boolean.TRUE.equals(req.getApprove())) {
            // transfer ownership
            User adopter = userRepository.findById(pet.getAdoptionRequestedBy())
                    .orElseThrow(() -> new IllegalArgumentException("Adopter not found"));
            pet.setOwner(adopter);
            pet.setAdoptionStatus(AdoptionStatus.ADOPTED);
            pet.setStatus(PetStatus.ACTIVE);
        } else {
            pet.setAdoptionStatus(AdoptionStatus.AVAILABLE);
            pet.setAdoptionRequestedBy(null);
            pet.setAdoptionRequestJson(null);
            pet.setAdoptionRequestedAt(null);
        }
        petRepository.save(pet);
        return toPetResponse(pet);
    }
}
