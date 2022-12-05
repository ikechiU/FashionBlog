package com.example.blog.service;

import com.example.blog.models.request.LoginRequest;
import com.example.blog.shared.dto.UserDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserDto createUser(@Valid UserDto userDto);
    UserDto authenticate(UserDto userDto);
    UserDto updateUser(String userId, @Valid UserDto userDto);
    UserDto getUser(String userId);
    List<UserDto> getUsers(int page, int limit);
    UserDto deleteUser(String userId);
    UserDto getUserDetails(String username);
}
