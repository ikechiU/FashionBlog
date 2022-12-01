package com.example.blog.models.response;

import lombok.Builder;
import lombok.Data;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Builder
public class ApiResponse<T> {
    private int httpStatusCode;
    private HttpStatus status;
    private List<Object> message;
    private boolean success;
    private T data;
    private LocalDateTime responseDate;
}
