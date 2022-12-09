package com.example.blog.service.impl;

import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.FileSystemRepository;
import com.example.blog.repositories.PostRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.PostService;
import com.example.blog.shared.dto.PostDto;
import com.example.blog.shared.utils.Utils;
import javax.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.util.Base64;
import java.util.List;

@Service
@AllArgsConstructor
@Validated
public class PostServiceImpl implements PostService {

    private final PostRepository postRepository;

    private final UserRepository userRepository;

    private final FileSystemRepository fileSystemRepository;

    private final Utils utils;

    @Override
    public PostDto createPost(MultipartFile file,  @Valid PostDto postDto) throws IOException {
        ModelMapper mapper = new ModelMapper();
        User user = getUserByUserId(postDto.getUserId());

        Post postToCreate = mapper.map(postDto, Post.class);
        postToCreate.setUserDetails(user);
        postToCreate.setPostId(utils.generatePostId(10));
        postToCreate.setImagePath(getImagePath(file));

        Post postCreated = postRepository.save(postToCreate);

        return mapper.map(postCreated, PostDto.class);
    }

    private String getImagePath(MultipartFile file) throws IOException{
        if (file != null) {
            if (file.getSize() > 0) {
                return fileSystemRepository.save(file.getBytes());
            }
        }
        return null;
    }

    @Override
    public PostDto updatePost(MultipartFile file,  @Valid PostDto postDto) throws IOException {
        ModelMapper mapper = new ModelMapper();
        getUserByUserId(postDto.getUserId());

        String postId = postDto.getPostId();
        Post post = getPostByPostId(postId);
        System.out.println(post);
        Post postToUpdate = mapper.map(post, Post.class);
        postToUpdate.setMessage(postDto.getMessage());

        if (post.getImagePath() != null) {
            deleteImage(post);
        }
        postToUpdate.setImagePath(getImagePath(file));

        Post postUpdated = postRepository.save(postToUpdate);
        return mapper.map(postUpdated, PostDto.class);
    }

    private boolean deleteImage(Post post) {
        File fileToDelete = new File(post.getImagePath());
        return fileToDelete.delete();
    }

    @Override
    public PostDto getPost(String postId) {
        ModelMapper mapper = new ModelMapper();
        Post post = getPostByPostId(postId);
        return mapper.map(post, PostDto.class);
    }

    @Override
    public List<PostDto> getPosts(String q, int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit);
        Page<Post> postPage;
        if (q.equals("")) postPage = postRepository.findAll(pageable);
        else postPage = postRepository.findAByMessageContainingIgnoreCase(q, pageable);
        List<Post> posts = postPage.getContent();
        Type dtoType = new TypeToken<List<PostDto>>() {
        }.getType();
        return new ModelMapper().map(posts, dtoType);
    }

    @Override
    public PostDto deletePost(String postId) {
        Post post = getPostByPostId(postId);
        postRepository.delete(post);
        return null;
    }

    private User getUserByUserId(String userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.WRONG_USER_ID.getErrorMessage()));
    }

    private Post getPostByPostId(String postId) {
        return postRepository.findByPostId(postId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.WRONG_POST_ID.getErrorMessage()));
    }

}
