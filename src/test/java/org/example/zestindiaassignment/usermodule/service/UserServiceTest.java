package org.example.zestindiaassignment.usermodule.service;

import jakarta.persistence.EntityManager;
import org.example.zestindiaassignment.security.JwtTokenProvider;
import org.example.zestindiaassignment.usermodule.dto.LoginRequestDto;
import org.example.zestindiaassignment.usermodule.dto.LoginResponseDto;
import org.example.zestindiaassignment.usermodule.dto.UserRegistrationDto;
import org.example.zestindiaassignment.usermodule.entity.User;
import org.example.zestindiaassignment.usermodule.repository.UserRepository;
import org.example.zestindiaassignment.usermodule.service.impl.UserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("User Service Unit Tests")
class UserServiceTest {

    @Mock private UserRepository        userRepository;
    @Mock private PasswordEncoder       passwordEncoder;
    @Mock private AuthenticationManager authenticationManager;
    @Mock private JwtTokenProvider      jwtTokenProvider;
    @Mock private EntityManager         entityManager;

    @InjectMocks
    private UserServiceImpl userService;

    private UserRegistrationDto registrationDto;
    private LoginRequestDto     loginRequestDto;

    @BeforeEach
    void setUp() {
        registrationDto = UserRegistrationDto.builder()
                .username("testuser")
                .email("test@example.com")
                .password("pass123")
                .fullName("Test User")
                .build();

        loginRequestDto = new LoginRequestDto("testuser", "pass123");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("registerUser - should throw when username already taken")
    void register_DuplicateUsername() {
        when(userRepository.existsByUsername("testuser")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registrationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already taken");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("registerUser - should throw when email already registered")
    void register_DuplicateEmail() {
        when(userRepository.existsByUsername("testuser")).thenReturn(false);
        when(userRepository.existsByEmail("test@example.com")).thenReturn(true);

        assertThatThrownBy(() -> userService.registerUser(registrationDto))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("already registered");
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("loginUser - should return token response on valid credentials")
    void login_Success() {
        Authentication auth = mock(Authentication.class);
        User mockUser = User.builder()
                .userId("USR2025-0001")
                .username("testuser")
                .email("test@example.com")
                .password("encodedPass")
                .role(User.Role.USER)
                .build();

        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenReturn(auth);
        when(jwtTokenProvider.generateToken(auth)).thenReturn("mock.jwt.token");
        when(jwtTokenProvider.getJwtExpirationMs()).thenReturn(86400000L);
        when(userRepository.findByUsernameOrEmail("testuser")).thenReturn(Optional.of(mockUser));

        LoginResponseDto result = userService.loginUser(loginRequestDto);

        assertThat(result).isNotNull();
        assertThat(result.getToken()).isEqualTo("mock.jwt.token");
        assertThat(result.getTokenType()).isEqualTo("Bearer");
        assertThat(result.getUsername()).isEqualTo("testuser");
        assertThat(result.getRole()).isEqualTo("USER");
        assertThat(result.getExpiresIn()).isEqualTo(86400000L);
    }

    // -------------------------------------------------------
    @Test
    @DisplayName("loginUser - should throw BadCredentialsException on wrong password")
    void login_BadCredentials() {
        when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
                .thenThrow(new BadCredentialsException("Bad credentials"));

        assertThatThrownBy(() -> userService.loginUser(loginRequestDto))
                .isInstanceOf(BadCredentialsException.class);
    }
}