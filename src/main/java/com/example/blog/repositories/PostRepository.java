package com.example.blog.repositories;

import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long> {
    Optional<Post> findByPostId(String postId);
    List<Post> findAllByUserDetails(User userDetails);
}
