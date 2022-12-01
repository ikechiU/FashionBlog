package com.example.blog.shared.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
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
    private String password;
    private String phoneNumber;
    private List<PostDto> postDtos;
}
