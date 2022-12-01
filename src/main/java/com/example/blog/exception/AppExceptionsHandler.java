package com.example.blog.exception;

import com.example.blog.models.response.ApiResponse;
import jdk.jshell.spi.ExecutionControl;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.time.Clock;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
@RestControllerAdvice
public class AppExceptionsHandler {
    private final Clock clock;

    private ApiResponse buildErrorResponse(Object error, HttpStatus status){
            return ApiResponse.builder()
                    .status(status)
                    .httpStatusCode(status.value())
                    .success(false)
                    .responseDate(LocalDateTime.now(clock))
                    .message(Collections.singletonList(error))
                    .build();
    }

    private static List<String> getValidationMessage(BindingResult bindingResult) {
        return bindingResult.getAllErrors()
                .stream()
                .map(AppExceptionsHandler::getValidationMessage)
                .collect(Collectors.toList());
    }
    private static String getValidationMessage(ObjectError error) {
        if (error instanceof FieldError fieldError) {
            String className = fieldError.getObjectName();
            String property = fieldError.getField();
            Object invalidValue = fieldError.getRejectedValue();
            String message = fieldError.getDefaultMessage();
            return String.format("%s.%s %s, but it was %s", className, property, message, invalidValue);
        }
        return String.format("%s: %s", error.getObjectName(), error.getDefaultMessage());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleValidationExceptions(MethodArgumentNotValidException e) {
        List<String> response = new ArrayList<>();
        e.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            String errorMessage = error.getDefaultMessage();
            response.add(String.format("%s : %s", fieldName, errorMessage));
        });
        log.error(e.getMessage());
        return buildErrorResponse(response, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(value = {BlogServerException.class})
    public ApiResponse handleServerException(BlogServerException ex) {
        return buildErrorResponse(ex.getMessage(), HttpStatus.INTERNAL_SERVER_ERROR);
    }

    @ExceptionHandler(value = IllegalStateException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleIllegalStateException(IllegalStateException ex) {
        log.error(ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = ExecutionControl.NotImplementedException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleNotImplementedException(ExecutionControl.NotImplementedException ex) {
        log.error(ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = BadRequestException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public ApiResponse handleBadRequest(BadRequestException ex) {
        log.error(ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
    @ExceptionHandler(value = UnAuthorizedException.class)
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public ApiResponse handleAuthorizedRequest(UnAuthorizedException ex) {
        log.error(ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.UNAUTHORIZED);
    }
    @ExceptionHandler(value = Exception.class)
    @ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
    public ApiResponse handleException(Exception ex) {
        log.error("ERROR OCCURRED! " + ex.getMessage());
        return buildErrorResponse(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
