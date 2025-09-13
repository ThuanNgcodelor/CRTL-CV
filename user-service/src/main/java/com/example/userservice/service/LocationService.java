package com.example.userservice.service;

import com.example.userservice.model.Address;
import com.example.userservice.model.ShelterProfile;
import com.example.userservice.model.VetProfile;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.repository.ShelterRepository;
import com.example.userservice.repository.VetProfileRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class LocationService {
    
    private final VetProfileRepository vetRepository;
    private final ShelterRepository shelterRepository;
    private final AddressRepository addressRepository;
    private final GeocodingService geocodingService;
    
    // Tìm vet gần nhất dựa trên địa chỉ mặc định của user
    public List<VetProfile> findNearbyVetsByUserAddress(String userId, double radiusKm) {
        Optional<Address> userAddressOpt = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        
        if (userAddressOpt.isEmpty()) {
            log.warn("User {} does not have a default address", userId);
            return List.of(); // Return empty list instead of throwing exception
        }
        
        Address userAddress = userAddressOpt.get();
        
        // Nếu chưa có tọa độ, thử geocoding
        if (userAddress.getLatitude() == null || userAddress.getLongitude() == null) {
            String fullAddress = userAddress.getStreetAddress() + ", " + userAddress.getProvince();
            var location = geocodingService.geocodeAddress(fullAddress);
            
            if (location.getLatitude() != null && location.getLongitude() != null) {
                userAddress.setLatitude(location.getLatitude());
                userAddress.setLongitude(location.getLongitude());
                userAddress.setCity(location.getCity());
                userAddress.setDistrict(location.getDistrict());
                addressRepository.save(userAddress);
                log.info("Successfully geocoded address for user {}: {}", userId, fullAddress);
            } else {
                log.error("Unable to geocode user address: {}", fullAddress);
                return List.of(); // Return empty list instead of throwing exception
            }
        }
        
        return findNearbyVets(userAddress.getLatitude(), userAddress.getLongitude(), radiusKm);
    }
    
    // Tìm vet gần nhất dựa trên tọa độ
    public List<VetProfile> findNearbyVets(double userLat, double userLng, double radiusKm) {
        return vetRepository.findAll().stream()
            .filter(vet -> vet.getClinicLocation() != null 
                    && vet.getClinicLocation().getLatitude() != null 
                    && vet.getClinicLocation().getLongitude() != null)
            .filter(vet -> vet.getClinicLocation().calculateDistance(userLat, userLng) <= radiusKm)
            .sorted((v1, v2) -> Double.compare(
                v1.getClinicLocation().calculateDistance(userLat, userLng),
                v2.getClinicLocation().calculateDistance(userLat, userLng)
            ))
            .collect(Collectors.toList());
    }
    
    // Tìm shelter gần nhất dựa trên địa chỉ mặc định của user
    public List<ShelterProfile> findNearbySheltersByUserAddress(String userId, double radiusKm) {
        Optional<Address> userAddressOpt = addressRepository.findByUserIdAndIsDefaultTrue(userId);
        
        if (userAddressOpt.isEmpty()) {
            log.warn("User {} does not have a default address", userId);
            return List.of(); // Return empty list instead of throwing exception
        }
        
        Address userAddress = userAddressOpt.get();
        
        // Nếu chưa có tọa độ, thử geocoding
        if (userAddress.getLatitude() == null || userAddress.getLongitude() == null) {
            String fullAddress = userAddress.getStreetAddress() + ", " + userAddress.getProvince();
            var location = geocodingService.geocodeAddress(fullAddress);
            
            if (location.getLatitude() != null && location.getLongitude() != null) {
                userAddress.setLatitude(location.getLatitude());
                userAddress.setLongitude(location.getLongitude());
                userAddress.setCity(location.getCity());
                userAddress.setDistrict(location.getDistrict());
                addressRepository.save(userAddress);
                log.info("Successfully geocoded address for user {}: {}", userId, fullAddress);
            } else {
                log.error("Unable to geocode user address: {}", fullAddress);
                return List.of(); // Return empty list instead of throwing exception
            }
        }
        
        return findNearbyShelters(userAddress.getLatitude(), userAddress.getLongitude(), radiusKm);
    }
    
    // Tìm shelter gần nhất dựa trên tọa độ
    public List<ShelterProfile> findNearbyShelters(double userLat, double userLng, double radiusKm) {
        return shelterRepository.findAll().stream()
            .filter(shelter -> shelter.getShelterLocation() != null 
                    && shelter.getShelterLocation().getLatitude() != null 
                    && shelter.getShelterLocation().getLongitude() != null)
            .filter(shelter -> shelter.getShelterLocation().calculateDistance(userLat, userLng) <= radiusKm)
            .sorted((s1, s2) -> Double.compare(
                s1.getShelterLocation().calculateDistance(userLat, userLng),
                s2.getShelterLocation().calculateDistance(userLat, userLng)
            ))
            .collect(Collectors.toList());
    }
    
    // Geocoding địa chỉ
    public com.example.userservice.model.Location geocodeAddress(String address) {
        return geocodingService.geocodeAddress(address);
    }
    
    // Debug method - lấy tất cả vet có tọa độ
    public List<VetProfile> getAllVetsWithLocation() {
        return vetRepository.findAll().stream()
            .filter(vet -> vet.getClinicLocation() != null 
                    && vet.getClinicLocation().getLatitude() != null 
                    && vet.getClinicLocation().getLongitude() != null)
            .collect(Collectors.toList());
    }
    
    // Kiểm tra xem user có địa chỉ mặc định không
    public boolean hasDefaultAddress(String userId) {
        return addressRepository.findByUserIdAndIsDefaultTrue(userId).isPresent();
    }
    
    // Tạo địa chỉ mặc định cho test
    public boolean createDefaultAddressForTest(String userId) {
        // Kiểm tra xem user đã có địa chỉ mặc định chưa
        if (hasDefaultAddress(userId)) {
            return false; // Đã có địa chỉ mặc định
        }
        
        try {
            // Tạo địa chỉ mặc định TP.HCM
            Address defaultAddress = Address.builder()
                .userId(userId)
                .addressName("Địa chỉ mặc định")
                .streetAddress("123 Nguyễn Huệ")
                .district("Quận 1")
                .city("TP.HCM")
                .province("TP.HCM")
                .isDefault(true)
                .latitude(10.762622)
                .longitude(106.660172)
                .build();
            
            addressRepository.save(defaultAddress);
            log.info("Created default address for user {}: {}", userId, defaultAddress.getStreetAddress());
            return true;
        } catch (Exception e) {
            log.error("Error creating default address for user {}: {}", userId, e.getMessage());
            throw e;
        }
    }
}
