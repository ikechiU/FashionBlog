package com.example.blog.shared.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PostLikeDto {
    private Long id;
    private boolean liked;
    private String postId;
    private String userId;
    private PostDto postDto;
}
