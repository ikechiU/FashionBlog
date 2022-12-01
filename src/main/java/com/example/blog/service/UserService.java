package com.example.blog.service;

import com.example.blog.shared.dto.UserDto;
import jakarta.validation.Valid;

import java.util.List;

public interface UserService {
    UserDto createUser(@Valid UserDto userDto);
    UserDto loginUser(String username, String password);
    UserDto updateUser(String userId, @Valid UserDto userDto);
    UserDto getUser(String userId);
    List<UserDto> getUsers(int page, int limit);
    UserDto deleteUser(String userId);
}
