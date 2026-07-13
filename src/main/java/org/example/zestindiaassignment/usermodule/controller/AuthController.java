package org.example.zestindiaassignment.usermodule.controller;

import jakarta.validation.Valid;
import lombok.*;
import lombok.extern.slf4j.Slf4j;
import org.example.zestindiaassignment.common.response.ApiResponse;
import org.example.zestindiaassignment.usermodule.dto.LoginRequestDto;
import org.example.zestindiaassignment.usermodule.dto.LoginResponseDto;
import org.example.zestindiaassignment.usermodule.dto.UserRegistrationDto;
import org.example.zestindiaassignment.usermodule.entity.User;
import org.example.zestindiaassignment.usermodule.service.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Authentication controller — public endpoints (no JWT needed).
 */
@Slf4j
@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    /**
     * POST /api/auth/register
     * Register a new user account.
     */
    @PostMapping("/register")
    public ResponseEntity<ApiResponse<UserResponseData>> registerUser(
            @Valid @RequestBody UserRegistrationDto dto) {

        log.info("Registration request for username: {}", dto.getUsername());

        User saved = userService.registerUser(dto);

        UserResponseData responseData = UserResponseData.builder()
                .userId(saved.getUserId())
                .username(saved.getUsername())
                .email(saved.getEmail())
                .fullName(saved.getFullName())
                .role(saved.getRole().name())
                .build();

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success("User registered successfully", responseData));
    }

    /**
     * POST /api/auth/login
     * Authenticate user and return JWT token.
     */
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponseDto>> loginUser(
            @Valid @RequestBody LoginRequestDto dto) {

        log.info("Login request for: {}", dto.getUsernameOrEmail());

        LoginResponseDto response = userService.loginUser(dto);

        return ResponseEntity.ok(
                ApiResponse.success("Login successful", response)
        );
    }

    // ---- Inner DTO for clean registration response ----
    @Data
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class UserResponseData {
        private String userId;
        private String username;
        private String email;
        private String fullName;
        private String role;
    }
}