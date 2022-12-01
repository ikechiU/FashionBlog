package com.example.blog.models.response;

import lombok.Data;

import java.util.List;

@Data
public class UserRest {
    private String userId;
    private String firstname;
    private String lastname;
    private String email;
    private String phoneNumber;
    private List<PostRest> postRests;
}
