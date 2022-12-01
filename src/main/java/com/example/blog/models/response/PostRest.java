package com.example.blog.models.response;

import lombok.Data;

import java.time.LocalDateTime;

@Data
public class PostRest {
    private String userId;
    private String postId;
    private String imagePath;
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
