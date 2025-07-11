package com.example.userservice.service;

import com.example.userservice.client.UserServiceClient;
import com.example.userservice.dto.UserDto;
import com.example.userservice.model.Address;
import com.example.userservice.repository.AddressRepository;
import com.example.userservice.request.AddressCreateRequest;
import com.example.userservice.request.AddressUpdateRequest;
import jakarta.ws.rs.NotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
public class AddressServiceImpl implements AddressService {

    private final UserServiceClient userServiceClient;
    private final AddressRepository addressRepository;

    @Override
    public Address SaveAddress(AddressCreateRequest addressCreateRequest) {
        Address address = Address.builder()
                .userId(addressCreateRequest.getUserId())
                .recipientName(addressCreateRequest.getRecipientName())
                .recipientPhone(addressCreateRequest.getRecipientPhone())
                .province(addressCreateRequest.getProvince())
                .streetAddress(addressCreateRequest.getStreetAddress())
                .isDefault(true)
                .build();
        return addressRepository.save(address);
    }

    @Override
    public List<Address> GetAllAddresses() {
        return addressRepository.findAll();
    }

    @Override
    public Address GetDefaultAddress(String userId) {
        return null;
    }

    @Override
    public Address UpdateAddress(AddressUpdateRequest addressUpdateRequest) {
        return null;
    }

    @Override
    public Address GetAddressById(String addressId) {
        return findAddressById(addressId);
    }

    @Override
    public void DeleteAddressById(String addressId) {
        Address address = findAddressById(addressId);
        addressRepository.delete(address);
    }

    @Override
    public Address SetDefaultAddress(String addressId, String userId) {
        Address address = findAddressById(addressId);
        UserDto user = getUserById(userId);
        if(!user.getId().equals(address.getUserId())) {
            return null;
        }

        List<Address> allAddresses = addressRepository.findAllByUserId(user.getId());
        for(Address a : allAddresses) {
            if(a.getIsDefault()){
                a.setIsDefault(false);
            }
        }
        address.setIsDefault(true);
        allAddresses.add(address);
        addressRepository.saveAll(allAddresses);
        return address;
    }

    protected UserDto getUserById(String userId) {
        return Optional.ofNullable(userServiceClient.getUserById(userId).getBody())
                .orElseThrow(() -> new NotFoundException("User not found with id: " + userId));
    }

    protected Address findAddressById(String addressId) {
        return addressRepository.findById(addressId)
                .orElseThrow(() -> new NotFoundException("Address not found with id: " + addressId));
    }

}
