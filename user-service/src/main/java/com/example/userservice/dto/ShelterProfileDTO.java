package com.example.userservice.dto;

import com.example.userservice.model.ShelterProfile;
import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ShelterProfileDTO {
    private String userId;
    private String shelterName;
    private String contactEmail;
    private String hotline;
    private String address;
    private String description;
    private Boolean verified;
    private List<String> adoptionListingIds;

    public static ShelterProfileDTO fromEntity(ShelterProfile profile) {
        ShelterProfileDTO dto = new ShelterProfileDTO();
        dto.setUserId(profile.getUserId());
        dto.setShelterName(profile.getShelterName());
        dto.setContactEmail(profile.getContactEmail());
        dto.setHotline(profile.getHotline());
        dto.setAddress(profile.getAddress());
        dto.setDescription(profile.getDescription());
        dto.setVerified(profile.getVerified());
        if (profile.getAdoptionListings() != null) {
            dto.setAdoptionListingIds(profile.getAdoptionListings().stream()
                    .map(listing -> listing.getId().toString())
                    .collect(Collectors.toList()));
        }
        return dto;
    }
}
