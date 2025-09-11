package com.example.userservice.controller;

import com.example.userservice.dto.*;
import com.example.userservice.jwt.JwtUtil;
import com.example.userservice.model.User;
import com.example.userservice.request.RegisterRequest;
import com.example.userservice.request.UserUpdateRequest;
import com.example.userservice.service.UserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/v1/user")
@RequiredArgsConstructor
public class UserController {
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    @GetMapping("/cart")
    ResponseEntity<CartDto> getCart(HttpServletRequest request) {

        CartDto cartDto = userService.getCart(request);
        return ResponseEntity.ok(cartDto);
    }

    @GetMapping("/information")
    ResponseEntity<UserInformationDto> getInformation(HttpServletRequest request){
        String userId = jwtUtil.ExtractUserId(request);
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(userId),UserInformationDto.class));
    }

    @PostMapping("/save")
    public ResponseEntity<UserDto> save(@Valid @RequestBody RegisterRequest request) {
        return ResponseEntity.ok(modelMapper.map(userService.SaveUser(request), UserDto.class));
    }

    @GetMapping("/getAll")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<UserAdminDto>> getAll() {
        return ResponseEntity.ok(userService.getAllUsers().stream().map(
                user -> modelMapper.map(user,UserAdminDto.class)
        ).toList());
    }
    @GetMapping("/getUserForAdminByUserId/{id}")
    public ResponseEntity<UserAdminDto> getUserForAdminByUserId(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(id), UserAdminDto.class));
    }

    @GetMapping("/getUserById/{id}")
    public ResponseEntity<UserDto> getUserById(@PathVariable String id) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserById(id), UserDto.class));
    }



    @GetMapping("/getUserByEmail/{email}")
    public ResponseEntity<AuthUserDto> getUserByEmail(@PathVariable String email) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserByEmail(email), AuthUserDto.class));
    }

    @GetMapping("/getUserByUsername/{username}")
    public ResponseEntity<AuthUserDto> getUserByUsername(@PathVariable String username) {
        return ResponseEntity.ok(modelMapper.map(userService.getUserByUsername(username), AuthUserDto.class));
    }

    @PutMapping(value = "/update", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#request.id).email == principal")
    public ResponseEntity<UserDto> updateUserById(
            @Valid @RequestPart("request") UserUpdateRequest request,
            @RequestPart(value = "file", required = false) MultipartFile file) {
        return ResponseEntity.ok(modelMapper.map(userService.updateUserById(request, file), UserDto.class));
    }

    @DeleteMapping("/deleteUserById/{id}")
    @PreAuthorize("hasRole('ADMIN') or @userService.getUserById(#request.id).email == principal")
    public ResponseEntity<Void> deleteUserById(@PathVariable String id) {
        userService.deleteUserById(id);
        return ResponseEntity.ok().build();
    }

    @PostMapping("/update-password")
    public ResponseEntity<Void> updatePassword(@RequestBody UpdatePassword request) {
        User user = userService.findUserByEmail(request.getEmail());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        return ResponseEntity.noContent().build();
    }

}
