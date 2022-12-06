package com.example.blog.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;


@Getter
@Setter
@Entity(name = "post_like")
public class PostLike {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private boolean liked;
    private String postId;
    private String userId;
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Post posts;
}
