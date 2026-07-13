package org.example.zestindiaassignment.usermodule.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.*;

/**
 * DTO for user login request.
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class LoginRequestDto {

    @NotBlank(message = "Username or email is required")
    private String usernameOrEmail;

    @NotBlank(message = "Password is required")
    private String password;
}