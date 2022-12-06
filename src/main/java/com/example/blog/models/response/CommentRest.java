package com.example.blog.models.response;


import lombok.Data;

import java.util.List;

@Data
public class CommentRest {
    private Long id;
    private String message;
    private String postId;
    private String userId;
    private String name;
    private List<CommentLikeRest> commentLikeRests;
}
