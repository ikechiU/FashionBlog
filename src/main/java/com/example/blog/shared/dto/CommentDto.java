package com.example.blog.shared.dto;


import lombok.Getter;
import lombok.Setter;

import java.util.List;
@Getter
@Setter
public class CommentDto {
    private long id;
    private String message;
    private String postId;
    private String userId;
    private String name;
    private PostDto postDto;
    private List<CommentLikeDto> commentLikeDtos;
}
