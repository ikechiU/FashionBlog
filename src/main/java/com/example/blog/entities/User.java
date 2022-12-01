package com.example.blog.entities;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
@Entity(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @NotBlank(message = "User id is mandatory")
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String password;
    private String phoneNumber;
    @OneToMany(mappedBy = "userDetails", cascade = CascadeType.ALL)
    private List<Post> posts;
}
