package com.example.blog.shared.dto;

import javax.validation.constraints.NotBlank;
import lombok.Data;

import java.util.Collection;
import java.util.List;

@Data
public class UserDto {
    private Long id;
    private String userId;
    @NotBlank(message = "Firstname is mandatory")
    private String firstname;
    @NotBlank(message = "Lastname is mandatory")
    private String lastname;
    private String email;
    private String token;
    private String username;
    private String password;
    private String phoneNumber;
    private String encryptedPassword;
    private String emailVerificationToken;
    private Boolean emailVerificationStatus = false;
    private List<PostDto> postDtos;
    private Collection<String> roles;
}
