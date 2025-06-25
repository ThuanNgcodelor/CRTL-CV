package com.example.jobservice.client;

import com.example.jobservice.dto.UserDto;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient(name = "user-service", path = "/v1/user-service")
public interface UserServiceClient {
    @GetMapping("/getUserById{userId}")
    ResponseEntity<UserDto> getUserById(@PathVariable String userId);
}
