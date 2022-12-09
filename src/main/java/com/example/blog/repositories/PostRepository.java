package com.example.blog.repositories;

import com.example.blog.entities.Post;
import com.example.blog.entities.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostRepository extends JpaRepository<Post, Long>, PagingAndSortingRepository<Post, Long> {
    Optional<Post> findByPostId(String postId);
    List<Post> findAllByUserDetails(User userDetails);
//    @Query(value = "select * from posts where message like '%:q%'", nativeQuery = true)
//    Page<Post> findAByMessageContainingIgnoreCase(@Param("q") String q, Pageable pageable);
    Page<Post> findAByMessageContainingIgnoreCase(String q, Pageable pageable);
}
