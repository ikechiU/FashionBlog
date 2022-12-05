package com.example.blog.models.request;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class LoginRequest {
    @NotBlank(message = "Username is mandatory, phone number or email")
    private String username;
    @NotBlank(message = "Password is mandatory")
    private String password;
}
