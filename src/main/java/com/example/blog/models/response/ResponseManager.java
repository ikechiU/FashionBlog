package com.example.blog.models.response;

import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.Collections;

@Service
@AllArgsConstructor
public class ResponseManager<T> {

    public ApiResponse<T> success(HttpStatus status, T data) {
        return new ApiResponse<>(
                status.value(),
                status,
                Collections.singletonList("Request successful"),
                true,
                data,
                LocalDateTime.now(Clock.systemUTC())
        );
    }

    public ApiResponse<T> error(HttpStatus status, String errorMessage) {
        return new ApiResponse<>(
                status.value(),
                status,
                Collections.singletonList(errorMessage),
                false,
                null,
                LocalDateTime.now(Clock.systemUTC())
        );
    }

}

