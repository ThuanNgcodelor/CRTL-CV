package com.example.authservice.service;

import com.example.authservice.client.UserServiceClient;
import com.example.authservice.dto.RegisterDto;
import com.example.authservice.dto.TokenDto;
import com.example.authservice.exception.WrongCredentialsException;
import com.example.authservice.request.LoginRequest;
import com.example.authservice.request.RegisterRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final AuthenticationManager authenticationManager;
    private final UserServiceClient userServiceClient;
    private final JwtService jwtService;

    public RegisterDto register(RegisterRequest request) {
        ResponseEntity<RegisterDto> response = userServiceClient.save(request);
        if (response == null || response.getBody() == null) {
            throw new RuntimeException("Failed to register user: No response from user service");
        }
        return response.getBody();
    }

    public TokenDto login(LoginRequest loginRequest) {
            Authentication authentication = authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(loginRequest.getEmail(), loginRequest.getPassword()));
            if (authentication.isAuthenticated())
                return TokenDto
                        .builder()
                        .token(jwtService.generateToken(loginRequest.getEmail()))
                        .build();
            else throw new WrongCredentialsException("Invalid email or password");
    }

}
