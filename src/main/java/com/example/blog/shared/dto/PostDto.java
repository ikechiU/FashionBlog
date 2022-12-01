package com.example.blog.shared.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;

@Data
public class PostDto {
    private Long id;
    @NotBlank(message = "Message is mandatory")
    private String userId;
    private String postId;
    private String imagePath;
    @NotBlank(message = "Message is mandatory")
    private String message;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
    private UserDto userDto;
    private MultipartFile file;
}
