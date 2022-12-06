package com.example.blog.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Entity(name = "comment_like")
public class CommentLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean liked;
    private String postId;
    private String userId;
    private Long commentId;
    @ManyToOne
    @JoinColumn(name = "comments_id")
    private Comment comments;
}
