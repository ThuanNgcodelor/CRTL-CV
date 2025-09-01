package com.example.authservice.dto;

import com.example.authservice.enums.Role;
import lombok.Data;

@Data
public class AuthUserDto {
    private String id;
    private String email;
    private String username;
    private String password;
    private Role role;
}