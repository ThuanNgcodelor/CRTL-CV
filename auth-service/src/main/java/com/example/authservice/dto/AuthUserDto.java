package com.example.authservice.dto;

import com.example.authservice.enums.Role;
import lombok.Data;

@Data
public class AuthUserDto {
    private String id;
    private String username;
    private String email;
    private String password;
    private Role role;
}