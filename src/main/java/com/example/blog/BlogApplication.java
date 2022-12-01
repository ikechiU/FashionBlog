package com.example.blog;

import jakarta.annotation.PostConstruct;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;

import java.time.Clock;
import java.util.TimeZone;

@SpringBootApplication
public class BlogApplication {

    public static void main(String[] args) {
        SpringApplication.run(BlogApplication.class, args);
    }

    @PostConstruct
    void setUTCTimezone() {
        TimeZone.setDefault(TimeZone.getTimeZone("UTC"));
    }

    @Bean
    public Clock clock() {
        return Clock.systemUTC();
    }

    @Bean
    public SpringApplicationContext springApplicationContext() {
        return new SpringApplicationContext();
    }


}
