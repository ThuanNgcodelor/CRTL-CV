package com.example.userservice.controller;

import com.example.userservice.dto.LocationDto;
import com.example.userservice.dto.NearbyShelterDto;
import com.example.userservice.dto.NearbyVetDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.model.Location;
import com.example.userservice.model.ShelterProfile;
import com.example.userservice.model.VetProfile;
import com.example.userservice.service.LocationService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/v1/user/location")
@RequiredArgsConstructor
public class LocationController {
    
    private final LocationService locationService;
    private final JwtUtil jwtUtil;
    
    // Tìm vet gần dựa trên địa chỉ mặc định của user   
    @GetMapping("/nearby/vets/my-address")
    public ResponseEntity<List<NearbyVetDto>> findNearbyVetsByMyAddress(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") double radiusKm) {
        
        String userId = jwtUtil.ExtractUserId(request);
        List<VetProfile> nearbyVets = locationService.findNearbyVetsByUserAddress(userId, radiusKm);
        
        List<NearbyVetDto> result = nearbyVets.stream()
            .map(this::convertToNearbyVetDto)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    // Tìm vet gần dựa trên tọa độ
    @GetMapping("/nearby/vets/coordinates")
    public ResponseEntity<List<NearbyVetDto>> findNearbyVetsByCoordinates(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm) {
        
        List<VetProfile> nearbyVets = locationService.findNearbyVets(latitude, longitude, radiusKm);
        
        List<NearbyVetDto> result = nearbyVets.stream()
            .map(vet -> {
                NearbyVetDto dto = convertToNearbyVetDto(vet);
                dto.setDistance(vet.getClinicLocation().calculateDistance(latitude, longitude));
                return dto;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    // Tìm shelter gần dựa trên địa chỉ mặc định của user
    @GetMapping("/nearby/shelters/my-address")
    public ResponseEntity<List<NearbyShelterDto>> findNearbySheltersByMyAddress(
            HttpServletRequest request,
            @RequestParam(defaultValue = "10") double radiusKm) {
        
        String userId = jwtUtil.ExtractUserId(request);
        List<ShelterProfile> nearbyShelters = locationService.findNearbySheltersByUserAddress(userId, radiusKm);
        
        List<NearbyShelterDto> result = nearbyShelters.stream()
            .map(this::convertToNearbyShelterDto)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    // Tìm shelter gần dựa trên tọa độ
    @GetMapping("/nearby/shelters/coordinates")
    public ResponseEntity<List<NearbyShelterDto>> findNearbySheltersByCoordinates(
            @RequestParam double latitude,
            @RequestParam double longitude,
            @RequestParam(defaultValue = "10") double radiusKm) {
        
        List<ShelterProfile> nearbyShelters = locationService.findNearbyShelters(latitude, longitude, radiusKm);
        
        List<NearbyShelterDto> result = nearbyShelters.stream()
            .map(shelter -> {
                NearbyShelterDto dto = convertToNearbyShelterDto(shelter);
                dto.setDistance(shelter.getShelterLocation().calculateDistance(latitude, longitude));
                return dto;
            })
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    // Kiểm tra xem user có địa chỉ mặc định không
    @GetMapping("/check-default-address")
    public ResponseEntity<Map<String, Object>> checkDefaultAddress(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        boolean hasDefaultAddress = locationService.hasDefaultAddress(userId);
        
        Map<String, Object> response = new HashMap<>();
        response.put("hasDefaultAddress", hasDefaultAddress);
        response.put("message", hasDefaultAddress ? 
            "User has default address" : 
            "User does not have a default address. Please set up your default address in profile settings.");
        
        return ResponseEntity.ok(response);
    }
    
    // Geocoding địa chỉ
    @PostMapping("/geocode")
    public ResponseEntity<LocationDto> geocodeAddress(@RequestBody String address) {
        Location location = locationService.geocodeAddress(address);
        
        LocationDto result = LocationDto.builder()
            .latitude(location.getLatitude())
            .longitude(location.getLongitude())
            .city(location.getCity())
            .district(location.getDistrict())
            .build();
        
        return ResponseEntity.ok(result);
    }
    
    // Debug endpoint - kiểm tra tất cả vet có tọa độ
    @GetMapping("/debug/vets-with-location")
    public ResponseEntity<List<NearbyVetDto>> debugVetsWithLocation() {
        List<VetProfile> allVets = locationService.getAllVetsWithLocation();
        
        List<NearbyVetDto> result = allVets.stream()
            .map(this::convertToNearbyVetDto)
            .collect(Collectors.toList());
        
        return ResponseEntity.ok(result);
    }
    
    // Debug endpoint - tạo địa chỉ mặc định cho user test
    @PostMapping("/debug/create-default-address")
    public ResponseEntity<Map<String, Object>> createDefaultAddressForTest(HttpServletRequest request) {
        String userId = jwtUtil.ExtractUserId(request);
        
        Map<String, Object> response = new HashMap<>();
        
        try {
            // Tạo địa chỉ mặc định TP.HCM
            boolean success = locationService.createDefaultAddressForTest(userId);
            
            if (success) {
                response.put("success", true);
                response.put("message", "Default address created successfully for testing");
                response.put("address", "123 Nguyễn Huệ, Quận 1, TP.HCM");
                response.put("coordinates", Map.of(
                    "latitude", 10.762622,
                    "longitude", 106.660172
                ));
            } else {
                response.put("success", false);
                response.put("message", "User already has a default address");
            }
        } catch (Exception e) {
            response.put("success", false);
            response.put("message", "Error creating default address: " + e.getMessage());
        }
        
        return ResponseEntity.ok(response);
    }
    
    private NearbyVetDto convertToNearbyVetDto(VetProfile vet) {
        LocationDto locationDto = null;
        if (vet.getClinicLocation() != null) {
            locationDto = LocationDto.builder()
                .latitude(vet.getClinicLocation().getLatitude())
                .longitude(vet.getClinicLocation().getLongitude())
                .city(vet.getClinicLocation().getCity())
                .district(vet.getClinicLocation().getDistrict())
                .build();
        }
        
        // Xử lý trường hợp UserDetails null
        String vetName = "Unknown";
        String vetPhone = "N/A";
        if (vet.getUser() != null && vet.getUser().getUserDetails() != null) {
            String firstName = vet.getUser().getUserDetails().getFirstName() != null ? 
                vet.getUser().getUserDetails().getFirstName() : "";
            String lastName = vet.getUser().getUserDetails().getLastName() != null ? 
                vet.getUser().getUserDetails().getLastName() : "";
            vetName = (firstName + " " + lastName).trim();
            if (vetName.isEmpty()) vetName = "Unknown";
            
            vetPhone = vet.getUser().getUserDetails().getPhoneNumber() != null ? 
                vet.getUser().getUserDetails().getPhoneNumber() : "N/A";
        }
        
        return NearbyVetDto.builder()
            .userId(vet.getUserId())
            .specialization(vet.getSpecialization())
            .yearsExperience(vet.getYearsExperience())
            .clinicAddress(vet.getClinicAddress())
            .bio(vet.getBio())
            .availableHoursJson(vet.getAvailableHoursJson())
            .clinicLocation(locationDto)
            .vetName(vetName)
            .vetPhone(vetPhone)
            .vetEmail(vet.getUser() != null ? vet.getUser().getEmail() : "N/A")
            .build();
    }
    
    private NearbyShelterDto convertToNearbyShelterDto(ShelterProfile shelter) {
        LocationDto locationDto = null;
        if (shelter.getShelterLocation() != null) {
            locationDto = LocationDto.builder()
                .latitude(shelter.getShelterLocation().getLatitude())
                .longitude(shelter.getShelterLocation().getLongitude())
                .city(shelter.getShelterLocation().getCity())
                .district(shelter.getShelterLocation().getDistrict())
                .build();
        }
        
        return NearbyShelterDto.builder()
            .userId(shelter.getUserId())
            .shelterName(shelter.getShelterName())
            .contactEmail(shelter.getContactEmail())
            .hotline(shelter.getHotline())
            .address(shelter.getAddress())
            .description(shelter.getDescription())
            .verified(shelter.getVerified())
            .shelterLocation(locationDto)
            .build();
    }
}
