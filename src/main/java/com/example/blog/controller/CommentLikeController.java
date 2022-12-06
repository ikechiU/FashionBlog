package com.example.blog.controller;

import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.CommentLikeRest;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.service.CommentLikeService;
import com.example.blog.shared.dto.CommentLikeDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CommentLikeController {

    private final CommentLikeService commentLikeService;

    @PutMapping(path = "/{userId}/post/{postId}/comment/{commentId}/comment-like",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<CommentLikeRest> updateCommentLike(@PathVariable Long commentId, @PathVariable String postId,
                                                          @PathVariable String userId) {
        CommentLikeDto commentLikeDto = commentLikeService.updateCommentLike(commentId, postId, userId);
        CommentLikeRest commentLikeRest = new ModelMapper().map(commentLikeDto, CommentLikeRest.class);
        return new ResponseManager<CommentLikeRest>().success(HttpStatus.OK, commentLikeRest);
    }


}
