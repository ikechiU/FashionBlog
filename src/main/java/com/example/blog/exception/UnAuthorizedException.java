package com.example.blog.exception;

public class UnAuthorizedException extends RuntimeException {
    public UnAuthorizedException(String errorMessage) {
        super(errorMessage);
    }
}
