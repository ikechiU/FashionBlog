package com.example.blog.service;

import com.example.blog.shared.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto comment(CommentDto commentDto, String userId, String postId);
    CommentDto updateComment(CommentDto commentDto, String userId, String postId, Long commentId);
    CommentDto getCommentById(String userId, String postId, Long commentId);

    List<CommentDto> getComments(String userId, String postId, int cPage, int cLimit);

    CommentDto deleteComment(String userId, String postId, Long commentId);
}
