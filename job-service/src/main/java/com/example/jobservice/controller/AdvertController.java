package com.example.jobservice.controller;

import com.example.jobservice.dto.AdvertDto;
import com.example.jobservice.enums.Advertiser;
import com.example.jobservice.request.advert.AdvertCreateRequest;
import com.example.jobservice.request.advert.AdvertUpdateRequest;
import com.example.jobservice.service.advert.AdvertService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/job/advert")
public class AdvertController {
    private final AdvertService advertService;
    private final ModelMapper modelMapper;

    @PostMapping("/create")
    public ResponseEntity<AdvertDto> createAdvert(@RequestPart @Valid AdvertCreateRequest request, MultipartFile file) {
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper
                        .map(advertService.createAdvert(request,file), AdvertDto.class));
    }

    @GetMapping("/getAll")
    public ResponseEntity<List<AdvertDto>> getAll(){
        return ResponseEntity.ok(advertService.getAll()
                .stream()
                .map(map-> modelMapper.map(map, AdvertDto.class)).toList());
    }

    @GetMapping("/getAdvertById/{id}")
    public ResponseEntity<AdvertDto> getAdvertById(@PathVariable String id){
        return ResponseEntity.ok(modelMapper.map(advertService.getAdvertById(id), AdvertDto.class));
    }

    @GetMapping("/getAdvertByUserId/{userId}")
    public ResponseEntity<List<AdvertDto>> getAdvertByUserId(@PathVariable String userId, @RequestParam Advertiser advertiser){
        return ResponseEntity.ok(advertService.getAdvertByUserId(userId,advertiser).stream()
                .map(advert -> modelMapper.map(advert, AdvertDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @advertServiceImpl.authorizeCheck(#request.id, principal)")
    public ResponseEntity<AdvertDto> updateAdvertById(@Valid @RequestPart AdvertUpdateRequest request,
                                                      @RequestPart(required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(advertService.updateAdvertById(request,file), AdvertDto.class));
    }

    @DeleteMapping("/delete/{advertId}")
    @PreAuthorize("hasRole('ADMIN') or @advertServiceImpl.authorizeCheck(#advertId, principal)")
    public ResponseEntity<Void> deleteAdvertById(@PathVariable String advertId) {
        advertService.deleteAdvertById(advertId);
        return ResponseEntity.ok().build();
    }
}
