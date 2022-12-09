package com.example.blog.controller;

import com.example.blog.entities.Comment;
import com.example.blog.models.request.CommentRequest;
import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.CommentRest;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.service.CommentService;
import com.example.blog.service.impl.CommentServiceImpl;
import com.example.blog.shared.dto.CommentDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.beans.BeanUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class CommentController {

    private final CommentServiceImpl commentService;

    @PostMapping(path = "/{userId}/post/{postId}/comment", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<CommentRest> comment(@PathVariable String userId, @PathVariable String postId, @RequestBody @Valid CommentRequest comment) {
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(comment, commentDto);
        CommentDto returnedCommentDto = commentService.comment(commentDto, userId, postId);
        CommentRest commentRest = new ModelMapper().map(returnedCommentDto, CommentRest.class);
        return new ResponseManager<CommentRest>().success(HttpStatus.OK, commentRest);
    }

    @PutMapping(path = "/{userId}/post/{postId}/comment/{commentId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<CommentRest> updateComment(@PathVariable String userId, @PathVariable String postId,
                                        @PathVariable Long commentId, @RequestBody @Valid CommentRequest comment) {
        CommentDto commentDto = new CommentDto();
        BeanUtils.copyProperties(comment, commentDto);
        CommentDto returnedCommentDto = commentService.updateComment(commentDto, userId, postId, commentId);
        CommentRest commentRest = new ModelMapper().map(returnedCommentDto, CommentRest.class);
        return new ResponseManager<CommentRest>().success(HttpStatus.OK, commentRest);
    }

    @GetMapping(path = "/{userId}/post/{postId}/comment/{commentId}",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<CommentRest> getCommentById(@PathVariable String userId, @PathVariable String postId,
                                                   @PathVariable Long commentId) {
        CommentDto commentDto = commentService.getCommentById(userId, postId, commentId);
        CommentRest commentRest = new ModelMapper().map(commentDto, CommentRest.class);
        return new ResponseManager<CommentRest>().success(HttpStatus.OK, commentRest);
    }

    @GetMapping(path = "/{userId}/post/{postId}/comment",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<List<CommentRest>> getComments(@PathVariable String userId, @PathVariable String postId,
                                                   @RequestParam(value = "cPage", defaultValue = "0") int cPage,
                                   @RequestParam(value = "cLimit", defaultValue = "5") int cLimit) {

        List<CommentDto> comments = commentService.getComments(userId, postId, cPage, cLimit);
        Type dtoType = new TypeToken<List<CommentRest>>() {}.getType();
        List<CommentRest> commentRests = new ModelMapper().map(comments, dtoType);
        return new ResponseManager<List<CommentRest>>().success(HttpStatus.OK, commentRests);
    }

    @DeleteMapping(path = "/{userId}/post/{postId}/comment/{commentId}/delete",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<CommentRest> deleteComment(@PathVariable String userId, @PathVariable String postId,
                                     @PathVariable Long commentId) {
        commentService.deleteComment(userId, postId, commentId);
        return new ResponseManager<CommentRest>().success(HttpStatus.OK, null);
    }

}
