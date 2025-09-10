package com.example.userservice.service;

import com.example.userservice.repository.PetRepository;
import com.example.userservice.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PetService {
    private final ModelMapper modelMapper;
    private final PetRepository petRepository;
    private final UserRepository userRepository;

}
