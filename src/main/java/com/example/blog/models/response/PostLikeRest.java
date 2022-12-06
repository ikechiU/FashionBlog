package com.example.blog.models.response;

import lombok.Data;

@Data
public class PostLikeRest {
    private Long id;
    private boolean liked;
    private String postId;
    private String userId;
}
