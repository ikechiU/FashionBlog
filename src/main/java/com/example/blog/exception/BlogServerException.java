package com.example.blog.exception;

public class BlogServerException extends RuntimeException {
    public BlogServerException(String errorMessage) {
        super(errorMessage);
    }
}
