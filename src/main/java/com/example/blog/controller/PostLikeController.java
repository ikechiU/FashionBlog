package com.example.blog.controller;

import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.PostLikeRest;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.service.PostLikeService;
import com.example.blog.shared.dto.PostLikeDto;
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
public class PostLikeController {

    private final PostLikeService postLikeService;

    @PutMapping(path = "/{userId}/post/{postId}/post-like",
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<PostLikeRest> updatePostLike(@PathVariable String userId, @PathVariable String postId) {
        PostLikeDto postLikeDto = postLikeService.updatePostLike(postId, userId);
        PostLikeRest postLikeRest = new ModelMapper().map(postLikeDto, PostLikeRest.class);
        return new ResponseManager<PostLikeRest>().success(HttpStatus.OK, postLikeRest);
    }

}
