package com.example.blog.service.impl;

import com.example.blog.entities.CommentLike;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.CommentLikeRepository;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.CommentLikeService;
import com.example.blog.shared.dto.CommentLikeDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CommentLikeServiceImpl implements CommentLikeService {

    private final CommentLikeRepository commentLikeRepository;
    private final CommentRepository commentRepository;
    private final UserRepository userRepository;

    private final PostRepository postRepository;

    @Override
    public CommentLikeDto updateCommentLike(Long commentId, String postId, String userId) {

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.WRONG_USER_ID.getErrorMessage()));

        postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.WRONG_POST_ID.getErrorMessage()));

        var comment = commentRepository.findById(commentId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        CommentLike commentLike = commentLikeRepository
                .findByCommentIdAndPostIdAndUserId(commentId, postId, userId);
        CommentLike comLike = new CommentLike();
        comLike.setLiked(true);

        if (commentLike != null) {
            var like = commentLike.isLiked();
            comLike.setLiked(!like);
            comLike.setId(commentLike.getId());
        }

        comLike.setUserId(userId);
        comLike.setPostId(postId);
        comLike.setCommentId(commentId);
        comLike.setComments(comment);

        var createdCommLike = commentLikeRepository.save(comLike);
        return new ModelMapper().map(createdCommLike, CommentLikeDto.class);
    }


}