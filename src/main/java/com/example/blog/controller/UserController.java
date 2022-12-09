package com.example.blog.controller;

import com.example.blog.models.request.*;
import com.example.blog.models.response.ApiResponse;
import com.example.blog.models.response.AuthRest;
import com.example.blog.models.response.ResponseManager;
import com.example.blog.models.response.UserRest;
import com.example.blog.service.UserService;
import com.example.blog.shared.dto.UserDto;
import javax.validation.Valid;

import io.swagger.v3.oas.annotations.security.SecurityRequirements;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.lang.reflect.Type;
import java.util.List;

@RestController
@RequestMapping("/users")
@AllArgsConstructor
public class UserController {

    private final UserService userService;

    @SecurityRequirements
    @PostMapping(path = {"/register", "/register/"}, consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> createUser(@RequestBody @Valid UserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.createUser(userDto), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.CREATED, userRest);
    }


    @PreAuthorize("hasAuthority('PRIVILEGE_AUTHORITY') or hasRole('ROLE_SUPER_ADMIN')")
//    @Secured("ROLE_ADMIN")
    @PostMapping(path = "/admin/register/{adminId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> createUserByAdmin(@PathVariable String adminId, @RequestBody @Valid AdminUserRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.createUserByAdmin(adminId, userDto), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.CREATED, userRest);
    }

    @SecurityRequirements
    @PostMapping("/login")
    public ResponseEntity<AuthRest> authenticate(@RequestBody @Valid LoginRequest loginRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(loginRequest, UserDto.class);
        UserDto authenticatedUserDto = userService.authenticate(userDto);
        AuthRest authRest = mapper.map(authenticatedUserDto, AuthRest.class);
        return new ResponseEntity<>(authRest, HttpStatus.ACCEPTED);
    }

    @PutMapping(path = "/{userId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> updateUser(@PathVariable String userId, @RequestBody @Valid UserUpdateRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.updateUser(userId, userDto), UserRest.class);
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }

//    @PreAuthorize("hasRole('ROLE_ADMIN')")
//    @Secured("ROLE_ADMIN")
    @PreAuthorize("hasAuthority('PRIVILEGE_AUTHORITY') or hasRole('ROLE_SUPER_ADMIN')")
    @PutMapping(path = "/admin/update/{adminId}", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE},
            produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> updateUserByAdmin(@PathVariable String adminId, @RequestBody @Valid AdminUpdateRequest userRequest) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        UserDto userDto = mapper.map(userRequest, UserDto.class);
        UserRest userRest = mapper.map(userService.updateUserByAdmin(adminId, userDto), UserRest.class);
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

    @PreAuthorize("hasAuthority('PRIVILEGE_AUTHORITY') or #userId == principal.userId")
    @DeleteMapping(path = "/{userId}", produces = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_XML_VALUE})
    public ApiResponse<UserRest> deleteUser(@PathVariable String userId) {
        UserRest userRest = new UserRest();
        UserDto userDto = userService.deleteUser(userId);
        if (userDto == null) userRest = null;
        return new ResponseManager<UserRest>().success(HttpStatus.OK, userRest);
    }
}
