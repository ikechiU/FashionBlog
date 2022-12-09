package com.example.blog.security;

import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.UserRepository;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.WebSecurityCustomizer;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Arrays;


@Configuration
@RequiredArgsConstructor
public class BeansConfiguration {

    private final UserRepository userRepository;

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    @Bean
    public UserDetailsService userDetailsService() {
        return username -> userRepository.findByEmailOrPhoneNumber(username, username)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.INCORRECT_LOGIN_CREDENTIALS.getErrorMessage()));
    }

    @Bean
    public AuthenticationProvider authenticationProvider() {
        DaoAuthenticationProvider authenticationProvider = new DaoAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService());
        authenticationProvider.setPasswordEncoder(passwordEncoder());
        return authenticationProvider;
    }

    @Bean
    public WebSecurityCustomizer webSecurityCustomizer() {
        return (web) -> web.ignoring()
                .antMatchers(
                        "/v2/api-docs",
                        "/swagger-ui/**",
                        "/v3/api-docs/**",
                        "/configuration/**",
                        "/swagger*/**",
                        "/swagger.json",
                        "/webjars/**",
                        "/swagger-resources/**",
                        "/");
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

//    @Bean
//    public OpenAPI customOpenAPI() {
//        return new OpenAPI()
//                .info(new Info()
//                        .title("Fashion Blog")
//                        .version("1.0")
//                        .description("A sample fashion blog Restful web service"))
//                .addSecurityItem(new SecurityRequirement()
//                        .addList("Authorization"))
//                .components(new Components()
//                        .addSecuritySchemes("my security", new SecurityScheme()
//                                .name("my security")
//                                .type(SecurityScheme.Type.HTTP)
//                                .scheme("bearer")));
//    }

    @Bean
    public OpenAPI customOpenAPI() {
        return new OpenAPI()
                .components(new Components().addSecuritySchemes("bearer-jwt",
                        new SecurityScheme().type(SecurityScheme.Type.HTTP).scheme("bearer").bearerFormat("JWT")
                                .in(SecurityScheme.In.HEADER).name("Authorization")))
                .info(new Info().title("Fashion Blog").version("snapshot").description("A sample fashion blog Restful web service"))
                .addSecurityItem(
                        new SecurityRequirement().addList("bearer-jwt", Arrays.asList("read", "write")));
    }


}
