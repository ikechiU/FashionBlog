package com.example.blog.entities;


import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.util.List;


@Getter
@Setter
@Entity(name = "comments")
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String message;
    private String postId;
    private String userId;
    private String name;
    @ManyToOne
    @JoinColumn(name = "posts_id")
    private Post posts;
    @OneToMany(mappedBy = "comments", cascade = CascadeType.ALL)
    private List<CommentLike> comment_likes;

}
