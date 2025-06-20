package com.example.userservice.service;

import com.example.userservice.model.User;
import com.example.userservice.model.UserDetails;
import com.example.userservice.request.RegisterRequest;
import com.example.userservice.request.UserUpdateRequest;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

public interface UserService {
    User SaveUser(RegisterRequest registerRequest);
    List<User> getAllUsers();
    public User getUserById(String id);
    public User getUserByEmail(String email);
    public User getUserByUsername(String username);
    User updateUserById(UserUpdateRequest request, MultipartFile file);
    void deleteUserById(String id);
    User findUserById(String id);
    User findUserByUsername(String username);
    User findUserByEmail(String email);
    UserDetails updateUserDetails(UserDetails toUpdate,UserDetails request, MultipartFile file);
}
