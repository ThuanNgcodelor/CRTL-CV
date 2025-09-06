package com.example.authservice.controller;

import com.example.authservice.dto.*;
import com.example.authservice.request.LoginRequest;
import com.example.authservice.request.RegisterRequest;
import com.example.authservice.request.GoogleLoginRequest;
import com.example.authservice.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    private final AuthService authService;

    @PostMapping("/login")
    public ResponseEntity<TokenDto> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(authService.login(request));
    }

    @PostMapping("/login/google")
    public ResponseEntity<TokenDto> loginWithGoogle(@Valid @RequestBody GoogleLoginRequest request) {
        return ResponseEntity.ok(authService.loginWithGoogle(request.getCode()));
    }

    @PostMapping("/register")
    public ResponseEntity<RegisterDto> register(@Valid  @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(authService.register(request));
    }

    @PostMapping("/forgotPassword")
    public ResponseEntity<?> forgotPassword(@Valid @RequestBody ForgotPassword request) {
        authService.forgotPassword(request);
        return ResponseEntity.ok("OTP sent to your email");
    }

    @PostMapping("/verifyOtp")
    public ResponseEntity<?> verifyOtp(@Valid @RequestBody VerifyOtp request) {
        boolean ok = authService.verifyOtp(request.getEmail(), request.getOtp());
        return ok ? ResponseEntity.ok("OTP verified successfully")
                : ResponseEntity.badRequest().body("Invalid OTP");
    }

    @PostMapping("/updatePassword")
    public ResponseEntity<?> updatePassword(@Valid @RequestBody UpdatePasswordRequest request) {
        boolean ok = authService.resetPassword(request);
        return ok ? ResponseEntity.ok("Password updated successfully")
                : ResponseEntity.badRequest().body("OTP not verified or verification expired");
    }


}
