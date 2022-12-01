package com.example.blog.controller;

import com.example.blog.models.request.LoginRequest;
import com.example.blog.models.request.UserRequest;
import com.example.blog.models.request.UserUpdateRequest;
import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.models.response.UserRest;
import com.example.blog.service.UserService;
import com.example.blog.shared.dto.UserDto;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @PostMapping(path = "register", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> createUser(@RequestBody UserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.createUser(userDto), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.CREATED, userRest);
    }

    @PostMapping(path = "login", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> loginUser(@RequestBody LoginRequest loginRequest) {
        ModelMapper mapper = new ModelMapper();
        UserRest userRest = mapper.map(userService.loginUser(loginRequest.getUsername(), loginRequest.getPassword()), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }

    @PutMapping(path = "/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> updateUser(@PathVariable String userId, @RequestBody UserUpdateRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.updateUser(userId, userDto), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }

    @GetMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> getUser(@PathVariable String userId) {
        ModelMapper mapper = new ModelMapper();
        UserRest userRest = mapper.map(userService.getUser(userId), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }

    @GetMapping(produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<List<UserRest>> getUsers(@RequestParam(value = "page", defaultValue = "0") int page,
                                                @RequestParam(value = "limit", defaultValue = "5") int limit) {
        ModelMapper mapper = new ModelMapper();
        Type restType = new TypeToken<List<UserRest>>() {
        }.getType();
        List<UserRest> userRests = mapper.map(userService.getUsers(page, limit), restType);
        return new ResponseManager<List<UserRest>>().success(HttpStatus.OK, userRests);
    }

    @DeleteMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> deleteUser(@PathVariable String userId) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.deleteUser(userId);
        if (userDto == null) userRest = null;
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }
}
