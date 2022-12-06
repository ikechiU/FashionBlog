package com.example.blog.service;


import com.example.blog.shared.dto.PostLikeDto;

public interface PostLikeService {
    PostLikeDto updatePostLike(String postId, String userId);
}
