package com.example.blog.models.response;

import lombok.Data;

@Data
public class AuthRest {
    private String token;
    private String userId;
}
