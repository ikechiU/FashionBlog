package com.example.blog.exception;

public enum ErrorMessages {
    MISSING_REQUIRED_FIELD("Missing required field(s)."),
    EMAIL_EMPTY("Email is empty."),
    INVALID_EMAIL("Email is invalid."),
    INCORRECT_LOGIN_REGISTRATION("Incorrect login credentials."),
    PHONE_NUMBER_EMPTY("Phone number is empty."),
    PASSWORD_EMPTY("Password is empty."),
    WRONG_USER_ID("User does not exist."),
    WRONG_POST_ID("Post does not exist."),
    POST_ID_REQUIRED("Post Id required."),
    RECORD_ALREADY_EXIST("Record already exist."),
    INCORRECT_LOGIN_DETAILS("Incorrect login details."),
    ALREADY_IN_PROGRESS_STATE("Already in progress state."),
    ALREADY_IN_PENDING_STATE("Already in pending state."),
    ALREADY_IN_DONE_STATE("Already in done state."),
    INCORRECT_STATUS_CHANGE("Status can only be changed to: \"in-progress\", \"pending\", or \"done\""),
    NO_RECORD_FOUND("No record found.");

    private String errorMessage;

    ErrorMessages(String errorMessage) {
        this.errorMessage = errorMessage;
    }

    public String getErrorMessage() {
        return errorMessage;
    }

    public void setErrorMessage(String errorMessage) {
        this.errorMessage = errorMessage;
    }
}
