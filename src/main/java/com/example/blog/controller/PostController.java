package com.example.blog.controller;

import com.example.blog.models.request.PostRequest;
import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.PostRest;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.models.response.UserRest;
import com.example.blog.service.PostService;
import com.example.blog.service.impl.PostServiceImpl;
import com.example.blog.shared.dto.PostDto;
import com.example.blog.shared.dto.UserDto;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.List;

@RestController
@AllArgsConstructor
@RequestMapping("/posts")
public class PostController {

    private final PostService postService;

    @PreAuthorize("hasRole('ROLE_PRIVILEGE') or hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @PostMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<PostRest> createPost(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "message") String message) throws IOException {

        PostDto postDto = new PostDto();
        postDto.setUserId(userId);
        postDto.setMessage(message);

        PostRest postRest = new ModelMapper().map(postService.createPost(file, postDto), PostRest.class);

        return new ResponseManager<PostRest>().success(HttpStatus.CREATED, postRest);
    }

    @PreAuthorize("hasRole('ROLE_PRIVILEGE') or hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @PutMapping(consumes = {MediaType.MULTIPART_FORM_DATA_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<PostRest> updatePost(
            @RequestParam(name = "file", required = false) MultipartFile file,
            @RequestParam(name = "userId") String userId,
            @RequestParam(name = "postId") String postId,
            @RequestParam(name = "message") String message) throws IOException {

        PostDto postDto = new PostDto();
        postDto.setUserId(userId);
        postDto.setPostId(postId);
        postDto.setMessage(message);

        PostRest postRest = new ModelMapper().map(postService.updatePost(file, postDto), PostRest.class);
        return new ResponseManager<PostRest>().success(HttpStatus.CREATED, postRest);
    }

    @GetMapping(path = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<PostRest> getPost(@PathVariable String postId) {
        ModelMapper mapper = new ModelMapper();
        PostRest postRest = mapper.map(postService.getPost(postId), PostRest.class);
        return new ResponseManager<PostRest>().success(HttpStatus.OK, postRest);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<List<PostRest>> getPosts(@RequestParam(value = "q", defaultValue = "") String q,
                                                @RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "limit", defaultValue = "5") int limit) {
        ModelMapper mapper = new ModelMapper();
        Type restType = new TypeToken<List<PostRest>>() {
        }.getType();
        List<PostRest> postRests = mapper.map(postService.getPosts(q, page, limit), restType);
        return new ResponseManager<List<PostRest>>().success(HttpStatus.OK, postRests);
    }

    @PreAuthorize("hasRole('ROLE_PRIVILEGE') or hasRole('ROLE_SUPER_ADMIN') or hasRole('ROLE_ADMIN')")
    @DeleteMapping(path = "/{postId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<PostRest> deletePost(@PathVariable String postId) {
        PostRest postRest = new PostRest();
        PostDto postDto = postService.deletePost(postId);
        if (postDto == null) postRest = null;
        return new ResponseManager<PostRest>().success(HttpStatus.OK, postRest);
    }
}
