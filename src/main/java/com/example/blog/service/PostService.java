package com.example.blog.service;
import com.example.blog.shared.dto.PostDto;
import jakarta.validation.Valid;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

public interface PostService {
    PostDto createPost(MultipartFile file, @Valid PostDto postDto) throws IOException;
    PostDto updatePost(MultipartFile file,  @Valid PostDto postDto) throws IOException;
    PostDto getPost(String postId);
    List<PostDto> getPosts(String message, int page, int limit);
    PostDto deletePost(String postId);
}
