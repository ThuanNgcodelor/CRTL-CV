package com.example.userservice.controller;

import com.example.userservice.dto.AddressDto;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.request.AddressCreateRequest;
import com.example.userservice.service.AddressService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/v1/user/address")
public class AddressController {
    private final AddressService addressService;
    private final JwtUtil jwtUtil;
    private final ModelMapper modelMapper;

    @PostMapping("/save")
    ResponseEntity<AddressDto> saveAddress(@RequestBody AddressCreateRequest addressCreateRequest, HttpServletRequest request){
        String userId = jwtUtil.ExtractUserId(request);
        addressCreateRequest.setUserId(userId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(modelMapper.map(addressService.SaveAddress(addressCreateRequest), AddressDto.class));
    }

    @GetMapping("/getAddressById/{addressId}")
    ResponseEntity<AddressDto> getAddressById(@PathVariable("addressId") String addressId){
        return ResponseEntity.ok(modelMapper.map(addressService.GetAddressById(addressId), AddressDto.class));
    }

    @DeleteMapping("/deleteAddressById/{addressId}")
    ResponseEntity<Void> deleteAddressById(@PathVariable("addressId") String addressId){
        addressService.DeleteAddressById(addressId);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/getAllAddresses")
    ResponseEntity<List<AddressDto>> getAllAddresses(){
        List<AddressDto> addressDto = addressService.GetAllAddresses().stream()
                .map(address -> modelMapper.map(address, AddressDto.class))
                .toList();
        return ResponseEntity.ok(addressDto);
    }
}
