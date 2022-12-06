package com.example.blog.service;

import com.example.blog.shared.dto.CommentLikeDto;;

public interface CommentLikeService {
    CommentLikeDto updateCommentLike(Long commentId, String postId, String userId);
}
