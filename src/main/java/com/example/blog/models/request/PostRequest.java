package com.example.blog.models.request;

import javax.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class PostRequest {
    @NotBlank(message = "UserId is mandatory")
    private String userId;
    @NotBlank(message = "Message is mandatory")
    private String message;
}
