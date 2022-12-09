package com.example.blog.security;

import com.example.blog.SpringApplicationContext;
import javax.servlet.http.Cookie;

public class SecurityConstants {
    public static final long EXPIRATION_TIME = 864000000; //10 days
    public static final long PASSWORD_RESET_EXPIRATION_TIME = 3600000; //1 hour
    public static final String HEADER_STRING = "Authorization";
    public static final String TOKEN_PREFIX = "Bearer";
    public static final String USER_ID = "User Id";

    public static final String LOGIN_USER = "users/login";
    public static final String REGISTER_USER = "users/register/";
    public static final String REGISTER_USER_2 = "users/register";
    public static final String EMAIL_VERIFICATION_URL = "users/email-verification";
    public static final String PASSWORD_RESET_REQUEST_URL = "users/password-reset-request";
    public static final String PASSWORD_RESET_URL = "users/password-reset";

    public static String getTokenSecret() {
        AppProperties appProperties = (AppProperties) SpringApplicationContext.getBean("AppProperties");
        return appProperties.geTokenSecret();
    }
}
