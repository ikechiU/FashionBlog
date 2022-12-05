package com.example.blog.service;

import com.example.blog.shared.dto.UserDto;

import java.util.List;

public interface UserService {
    UserDto createUser(UserDto userDto);
    UserDto createUserByAdmin(String adminId, UserDto userDto);
    UserDto authenticate(UserDto userDto);
    UserDto updateUser(String userId, UserDto userDto);
    UserDto updateUserByAdmin(String adminId, UserDto userDto);
    UserDto getUser(String userId);
    List<UserDto> getUsers(int page, int limit);
    UserDto deleteUser(String userId);
}
