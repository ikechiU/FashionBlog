package com.example.blog.models.response;

import lombok.Data;

import java.util.Collection;

@Data
public class AuthRest {
    private String token;
    private String userId;
    private Collection<String> roles;
}
