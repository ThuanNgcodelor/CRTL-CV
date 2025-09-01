package com.example.authservice.client;

import com.example.authservice.dto.AuthUserDto;
import com.example.authservice.dto.RegisterDto;
import com.example.authservice.dto.UpdatePassword;
import com.example.authservice.request.RegisterRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@FeignClient(name = "user-service", path = "/v1/user")
public interface UserServiceClient {
    @PostMapping("/save")
    ResponseEntity<RegisterDto> save(@RequestBody RegisterRequest request);
    @GetMapping(value = "/getUserByEmail/{email}",headers = "X-Internal-Call=true")
    ResponseEntity<AuthUserDto> getUserByEmail(@PathVariable String email);
    @PostMapping("/update-password")
    ResponseEntity<Void> updatePassword(@RequestBody UpdatePassword request);
}
