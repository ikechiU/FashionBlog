package com.example.blog.service.impl;

import com.example.blog.entities.Comment;
import com.example.blog.entities.Post;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.CommentRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.CommentService;
import com.example.blog.shared.dto.CommentDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.lang.reflect.Type;
import java.util.List;

@Service
@AllArgsConstructor
public class CommentServiceImpl implements CommentService {

    private final UserRepository userRepository;
    private final CommentRepository commentRepository;
    private final PostRepository postRepository;


    @Override
    public CommentDto comment(CommentDto commentDto, String userId, String postId) {

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));


        Comment comment = new ModelMapper().map(commentDto, Comment.class);
        comment.setPostId(postId);
        comment.setUserId(userId);
        comment.setPosts(postRepository.findByPostId(postId).get());
        comment.setPosts(post);

        Comment createdComment = commentRepository.save(comment);

        return new ModelMapper().map(createdComment, CommentDto.class);
    }

    @Override
    public CommentDto updateComment(CommentDto commentDto, String userId, String postId, Long commentId) {

        Comment comment  = getComment(commentId);

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Post post = postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Comment commentToUpdate = new ModelMapper().map(commentDto, Comment.class);
        commentToUpdate.setId(comment.getId());
        commentToUpdate.setPosts(post);

        Comment updatedComment = commentRepository.save(commentToUpdate);
        return new ModelMapper().map(updatedComment, CommentDto.class);
    }

    @Override
    public CommentDto getCommentById(String userId, String postId, Long commentId) {

        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Comment comment  = getComment(commentId);
        return new ModelMapper().map(comment, CommentDto.class);
    }

    private Comment getComment(Long commentId) {
       return commentRepository.findById(commentId)
               .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
    }

    @Override
    public List<CommentDto> getComments(String userId, String postId, int cPage, int cLimit) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        if (cPage > 0) cPage = cPage - 1;
        PageRequest cPageable = PageRequest.of(cPage, cLimit, Sort.by("id").descending());
        Page<Comment> commentEntities = commentRepository.findAllByPostId(postId, cPageable);

        Type cDtoType = new TypeToken<List<CommentDto>>() {}.getType();

        return new ModelMapper().map(commentEntities.getContent(), cDtoType);
    }

    @Override
    public CommentDto deleteComment(String userId, String postId, Long commentId) {
        userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        Comment comment = getComment(commentId);
        if (comment == null)
            throw new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage());

        commentRepository.delete(comment);

        return null;
    }

}
