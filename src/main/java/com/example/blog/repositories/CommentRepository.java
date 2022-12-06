package com.example.blog.repositories;

import com.example.blog.entities.Comment;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long>, PagingAndSortingRepository<Comment, Long> {
    Optional<Comment> findByPostId(String postId);
    Page<Comment> findAllByPostId(String postId, Pageable pageable);
    List<Comment> findAllByPostId(String postId);

}
