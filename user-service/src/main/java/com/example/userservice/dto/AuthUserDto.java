package com.example.userservice.dto;

import com.example.userservice.model.Role;
import lombok.Data;

@Data
public class AuthUserDto {
    private String id;
    private String username;
    private String password;
    private Role role;
}