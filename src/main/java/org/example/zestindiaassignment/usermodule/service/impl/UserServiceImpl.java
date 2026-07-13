package org.example.zestindiaassignment.usermodule.service.impl;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.example.zestindiaassignment.security.JwtTokenProvider;
import org.example.zestindiaassignment.usermodule.dto.LoginRequestDto;
import org.example.zestindiaassignment.usermodule.dto.LoginResponseDto;
import org.example.zestindiaassignment.usermodule.dto.UserRegistrationDto;
import org.example.zestindiaassignment.usermodule.entity.User;
import org.example.zestindiaassignment.usermodule.repository.UserRepository;
import org.example.zestindiaassignment.usermodule.service.UserService;
import org.example.zestindiaassignment.usermodule.utils.IdGeneratorUtil;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

/**
 * Implementation of UserService — handles registration and login.
 */
@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository      userRepository;
    private final PasswordEncoder     passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtTokenProvider    jwtTokenProvider;

    @PersistenceContext
    private EntityManager entityManager;

    @Override
    @Transactional
    public User registerUser(UserRegistrationDto dto) {
        log.debug("Registering user: {}", dto.getUsername());

        // --- Uniqueness checks ---
        if (userRepository.existsByUsername(dto.getUsername())) {
            throw new IllegalArgumentException(
                    "Username '" + dto.getUsername() + "' is already taken."
            );
        }
        if (userRepository.existsByEmail(dto.getEmail())) {
            throw new IllegalArgumentException(
                    "Email '" + dto.getEmail() + "' is already registered."
            );
        }

        // --- Generate unique ID: USR2025-XXXX ---
        String userId = IdGeneratorUtil.generate(
                entityManager,
                "USR",
                "users",
                "user_id",
                1000,
                9999
        );

        // --- Determine role ---
        User.Role role = User.Role.USER;
        if (dto.getRole() != null && dto.getRole().equalsIgnoreCase("ADMIN")) {
            role = User.Role.ADMIN;
        }

        // --- Build and save ---
        User user = User.builder()
                .userId(userId)
                .username(dto.getUsername())
                .email(dto.getEmail())
                .password(passwordEncoder.encode(dto.getPassword()))
                .fullName(dto.getFullName())
                .role(role)
                .isActive(true)
                .build();

        User saved = userRepository.save(user);
        log.info("User registered successfully. ID: {}", userId);
        return saved;
    }

    @Override
    public LoginResponseDto loginUser(LoginRequestDto dto) {
        log.debug("Login attempt: {}", dto.getUsernameOrEmail());

        // --- Authenticate via Spring Security ---
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        dto.getUsernameOrEmail(),
                        dto.getPassword()
                )
        );

        // --- Generate JWT token ---
        String token = jwtTokenProvider.generateToken(authentication);

        // --- Fetch user details ---
        User user = userRepository
                .findByUsernameOrEmail(dto.getUsernameOrEmail())
                .orElseThrow(() -> new RuntimeException("User not found"));

        log.info("User logged in: {}", user.getUsername());

        return LoginResponseDto.builder()
                .token(token)
                .tokenType("Bearer")
                .userId(user.getUserId())
                .username(user.getUsername())
                .email(user.getEmail())
                .role(user.getRole().name())
                .expiresIn(jwtTokenProvider.getJwtExpirationMs())
                .build();
    }
}