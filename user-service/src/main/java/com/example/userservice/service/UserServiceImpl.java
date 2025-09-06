package com.example.userservice.service;

import com.example.userservice.client.FileStorageClient;
import com.example.userservice.client.StockServiceClient;
import com.example.userservice.dto.CartDto;
import com.example.userservice.exception.NotFoundException;
import com.example.userservice.model.Active;
import com.example.userservice.model.Role;
import com.example.userservice.model.User;
import com.example.userservice.model.UserDetails;
import com.example.userservice.repository.UserRepository;
import com.example.userservice.request.RegisterRequest;
import com.example.userservice.request.UserUpdateRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RequiredArgsConstructor
@Service("userService")
public class UserServiceImpl implements UserService {
    private final FileStorageClient fileStorageClient;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final StockServiceClient stockServiceClient;

    @Override
    public CartDto getCart(HttpServletRequest request) {
        String author = request.getHeader("Authorization");
        ResponseEntity<CartDto> response = stockServiceClient.getCart(author);
        return response.getBody();
    }


    @Override
    public User SaveUser(RegisterRequest registerRequest) {
        User toSave = User.builder()
                .username(registerRequest.getUsername())
                .password(passwordEncoder.encode(registerRequest.getPassword()))
                .email(registerRequest.getEmail())
                .role(Role.USER)
                .active(Active.ACTIVE)
                .build();
        return userRepository.save(toSave);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAllByActive(Active.ACTIVE);
    }

    @Override
    public User getUserById(String id) {
        return findUserById(id);
    }

    @Override
    public User getUserByEmail(String email) {
        return findUserByEmail(email);
    }

    @Override
    public User getUserByUsername(String username) {
        return findUserByUsername(username);
    }

    @Override
    public User updateUserById(UserUpdateRequest request, MultipartFile file) {
        User toUpdate = findUserById(request.getId());
        request.setUserDetails(updateUserDetails(toUpdate.getUserDetails(), request.getUserDetails(), file));
        modelMapper.map(toUpdate, request);
        return userRepository.save(toUpdate);
    }

    @Override
    public void deleteUserById(String id) {
        User toDelete = findUserById(id);
        toDelete.setActive(Active.INACTIVE);
        userRepository.save(toDelete);
    }

    @Override
    public User findUserById(String id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User findUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new NotFoundException("User not found"));
    }

    @Override
    public UserDetails updateUserDetails(UserDetails toUpdate, UserDetails request, MultipartFile file) {
        toUpdate = toUpdate == null ? new UserDetails() : toUpdate;

        if (file != null) {
            String profilePicture = fileStorageClient.uploadImageToFIleSystem(file).getBody();
            if (profilePicture != null) {
                fileStorageClient.deleteImageFromFileSystem(toUpdate.getImageUrl());
                toUpdate.setImageUrl(profilePicture);
            }
        }

        modelMapper.map(request, toUpdate);

        return toUpdate;
    }

    @Override
    public void updatePasswordByEmail(String email, String rawPassword) {
        User user = findUserByEmail(email);
        user.setPassword(passwordEncoder.encode(rawPassword));
        userRepository.save(user);
    }
}
