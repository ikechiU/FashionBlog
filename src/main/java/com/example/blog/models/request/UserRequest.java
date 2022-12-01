package com.example.blog.models.request;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class UserRequest {
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;
}
