package com.example.jobservice.service.advert;

import com.example.jobservice.dto.UserDto;
import com.example.jobservice.enums.Advertiser;
import com.example.jobservice.model.Advert;
import com.example.jobservice.request.advert.AdvertCreateRequest;
import com.example.jobservice.request.advert.AdvertUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface AdvertService {
    Advert createAdvert(AdvertCreateRequest request, MultipartFile file);
    Advert updateAdvertById(AdvertUpdateRequest request, MultipartFile file);
    List<Advert> getAll();
    Advert getAdvertById(String id);
    void deleteAdvertById(String id);
    List<Advert> getAdvertByUserId(String userId, Advertiser advertiser);
    UserDto getUserById(String userId);
}
