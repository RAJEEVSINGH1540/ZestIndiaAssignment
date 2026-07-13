package org.example.zestindiaassignment.usermodule.service;

import org.example.zestindiaassignment.usermodule.dto.LoginRequestDto;
import org.example.zestindiaassignment.usermodule.dto.LoginResponseDto;
import org.example.zestindiaassignment.usermodule.dto.UserRegistrationDto;
import org.example.zestindiaassignment.usermodule.entity.User;

/**
 * Service interface for user management operations.
 */
public interface UserService {

    User registerUser(UserRegistrationDto dto);

    LoginResponseDto loginUser(LoginRequestDto dto);
}