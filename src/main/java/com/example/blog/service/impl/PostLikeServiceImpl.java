package com.example.blog.service.impl;

import com.example.blog.entities.Post;
import com.example.blog.entities.PostLike;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.PostLikeRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.PostLikeService;
import com.example.blog.shared.dto.PostLikeDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class PostLikeServiceImpl implements PostLikeService {

    private final PostLikeRepository postLikeRepository;
    private final PostRepository postRepository;
    private final UserRepository userRepository;


    @Override
    public PostLikeDto updatePostLike(String postId, String userId) {

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));


        PostLike postLikeEntity = postLikeRepository.findAllByPostIdAndUserId(postId, userId);
        PostLike postLike = new PostLike();
        postLike.setLiked(true);
        if (postLikeEntity != null) {
            var like = postLikeEntity.isLiked();
            postLike.setLiked(!like);
            postLike.setId(postLikeEntity.getId());
        }

        postLike.setPostId(postId);
        postLike.setUserId(userId);
        postLike.setPosts(post);

        PostLike updatedPostLike = postLikeRepository.save(postLike);
        return new ModelMapper().map(updatedPostLike, PostLikeDto.class);
    }

}
