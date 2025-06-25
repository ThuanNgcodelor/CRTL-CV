package com.example.jobservice.controller;

import com.example.jobservice.dto.OfferDto;
import com.example.jobservice.request.offer.MakeAnOfferRequest;
import com.example.jobservice.request.offer.OfferUpdateRequest;
import com.example.jobservice.service.offer.OfferService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RequiredArgsConstructor
@RestController
@RequestMapping("/v1/job/offer")
public class OfferController {
    private final OfferService offerService;
    private final ModelMapper modelMapper;

    @PostMapping("/makeAnOffer")
    public ResponseEntity<OfferDto> makeOffer(@Valid @RequestBody MakeAnOfferRequest request){
        return ResponseEntity.status(HttpStatus.CREATED).body(modelMapper.map(offerService.makeAnOffer(request), OfferDto.class));
    }

    @GetMapping("/getOfferById/{id}")
    public ResponseEntity<OfferDto> getOfferById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(offerService.getOfferById(id), OfferDto.class));
    }

    @GetMapping("/getOffersByUserId/{userId}")
    public ResponseEntity<List<OfferDto>> getOffersByUserId(@PathVariable String userId){
        return ResponseEntity.ok(offerService.getOffersByUserId(userId).stream()
                .map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @GetMapping("/getOffersByAdvertId/{advertId}")
    public ResponseEntity<List<OfferDto>> getOffersByAdvertId(@PathVariable String advertId){
        return ResponseEntity.ok(offerService.getOffersByAdvertId(advertId).stream().map(offer -> modelMapper.map(offer, OfferDto.class)).toList());
    }

    @PutMapping("/update")
    @PreAuthorize("hasRole('ADMIN') or @offerServiceImpl.authorizeCheck(#request.id, principal)")
    public ResponseEntity<OfferDto> updateOfferById(@Valid @RequestBody OfferUpdateRequest request){
        return ResponseEntity.ok(modelMapper.map(offerService.updateOfferById(request),OfferDto.class));
    }

    @DeleteMapping("/deleteOfferById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @offerServiceImpl.authorizeCheck(#id, principal)")
    public ResponseEntity<Void> deleteOfferById(@PathVariable String id) {
        offerService.deleteOfferById(id);
        return ResponseEntity.ok().build();
    }
}
