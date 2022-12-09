package com.example.blog.models.request;

import javax.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter
@Setter
@NoArgsConstructor
public class CommentRequest {
    @NotBlank(message = "Message is mandatory")
    private String message;
    @NotBlank(message = "PostId is mandatory")
    private String postId;
    @NotBlank(message = "UserId is mandatory")
    private String userId;
    @NotBlank(message = "Name is mandatory")
    private String name;
}
