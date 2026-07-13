package org.example.zestindiaassignment.usermodule.dto;

import lombok.*;

/**
 * DTO for login response — contains JWT token and user info.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class LoginResponseDto {

    private String token;
    private String tokenType;
    private String userId;
    private String username;
    private String email;
    private String role;
    private long expiresIn;
}