package com.example.blog.models.request;

import lombok.Data;

@Data
public class PostRequest {
    private String userId;
    private String message;
}
