package com.example.blog.repositories;

import com.example.blog.entities.CommentLike;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CommentLikeRepository extends JpaRepository<CommentLike, Long>, PagingAndSortingRepository<CommentLike, Long> {
    Optional<CommentLike> findByCommentId(Long commentId);
    List<CommentLike> findAllByPostIdAndLiked(String postId, boolean liked);
    List<CommentLike> findAllByCommentIdAndLiked(Long commentId, boolean liked);

    List<CommentLike> findAllByPostId(String postId);

    CommentLike findByCommentIdAndPostIdAndUserId(Long commentId, String postId, String userId);
}
