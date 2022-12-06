package com.example.blog.repositories;

import com.example.blog.entities.PostLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PostLikeRepository extends JpaRepository<PostLike, Long>, PagingAndSortingRepository<PostLike, Long> {
    Optional<PostLike> findByPostId(String postId);

    List<PostLike> findAllByPostIdAndLiked(String postId, boolean liked);
    PostLike findAllByPostIdAndUserId(String postId, String userId);

    List<PostLike> findAllByPostIdAndUserIdAndLiked(String postId, String userId, boolean liked);
}
