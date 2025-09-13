package com.example.userservice.service;

import com.example.userservice.model.Location;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;

@Service
@RequiredArgsConstructor
@Slf4j
public class GeocodingService {
    
    @Value("${google.maps.api.key:}")
    private String googleApiKey;
    
    private final RestTemplate restTemplate = new RestTemplate();
    
    public Location geocodeAddress(String address) {
        if (googleApiKey.isEmpty()) {
            log.warn("Google Maps API key not configured");
            return Location.builder().build();
        }
        
        try {
            String encodedAddress = URLEncoder.encode(address, StandardCharsets.UTF_8);
            String url = String.format(
                "https://maps.googleapis.com/maps/api/geocode/json?address=%s&key=%s",
                encodedAddress, googleApiKey
            );
            
            GeocodingResponse response = restTemplate.getForObject(url, GeocodingResponse.class);
            
            if (response != null && response.results != null && !response.results.isEmpty()) {
                GeocodingResult result = response.results.get(0);
                GeocodingLocation location = result.geometry.location;
                
                return Location.builder()
                    .latitude(location.lat)
                    .longitude(location.lng)
                    .city(extractCity(result.address_components))
                    .district(extractDistrict(result.address_components))
                    .build();
            }
        } catch (Exception e) {
            log.error("Error geocoding address: {}", address, e);
        }
        
        return Location.builder().build();
    }
    
    private String extractCity(java.util.List<AddressComponent> components) {
        return components.stream()
            .filter(comp -> comp.types.contains("locality") || comp.types.contains("administrative_area_level_2"))
            .map(comp -> comp.long_name)
            .findFirst()
            .orElse("");
    }
    
    private String extractDistrict(java.util.List<AddressComponent> components) {
        return components.stream()
            .filter(comp -> comp.types.contains("administrative_area_level_3") || comp.types.contains("sublocality"))
            .map(comp -> comp.long_name)
            .findFirst()
            .orElse("");
    }
    
    // Inner classes for JSON parsing
    public static class GeocodingResponse {
        public java.util.List<GeocodingResult> results;
    }
    
    public static class GeocodingResult {
        public GeocodingGeometry geometry;
        public java.util.List<AddressComponent> address_components;
    }
    
    public static class GeocodingGeometry {
        public GeocodingLocation location;
    }
    
    public static class GeocodingLocation {
        public double lat;
        public double lng;
    }
    
    public static class AddressComponent {
        public String long_name;
        public java.util.List<String> types;
    }
}
