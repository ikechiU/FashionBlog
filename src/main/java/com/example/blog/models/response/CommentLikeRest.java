package com.example.blog.models.response;

import lombok.Data;

@Data
public class CommentLikeRest {
    private Long id;
    private boolean liked;
    private String postId;
    private String userId;
}
