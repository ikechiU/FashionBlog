package com.example.blog.service.impl;

import com.example.blog.entities.User;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.exception.UnAuthorizedException;
import com.example.blog.repositories.UserRepository;
import com.example.blog.service.UserService;
import com.example.blog.shared.dto.UserDto;
import com.example.blog.shared.utils.Utils;
import com.twilio.Twilio;
import com.twilio.rest.api.v2010.account.Message;
import io.github.cdimascio.dotenv.Dotenv;
import jakarta.validation.Valid;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.validation.annotation.Validated;

import javax.naming.NamingException;
import java.lang.reflect.Type;
import java.util.List;
import java.util.Optional;

@Service
@AllArgsConstructor
@Validated
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Utils utils;

    @Override
    public UserDto createUser(@Valid UserDto userDto) {
        ModelMapper mapper = new ModelMapper();

        checkUserDtoError(userDto);
        User userToCreate = mapper.map(userDto, User.class);
        userToCreate.setUserId(utils.generateUserId(10));

        User createdUser = userRepository.save(userToCreate);

//        Dotenv dotenv = Dotenv.load();
//        String account_sid = dotenv.get("ACCOUNT_SID");
//        String auth_token = dotenv.get("AUTH_TOKEN");
//        String phone_number = dotenv.get("PHONE_NUMBER");

//        Twilio.init(account_sid, auth_token);
//        Message message = Message.creator(
//                new com.twilio.type.PhoneNumber(userDto.getPhoneNumber()),
//                new com.twilio.type.PhoneNumber(phone_number),
//                "TEST TWILIO"
//        ).create();
//
//        System.out.println(message.getSid());
        return mapper.map(createdUser, UserDto.class);
    }

    @Override
    public UserDto loginUser(String username, String password) {
        ModelMapper mapper = new ModelMapper();
        if (username.contains("@")) {
            checkEmailError(username);
            User user = userRepository.findByEmailAndPassword(username, password)
                    .orElseThrow(() -> new UnAuthorizedException(ErrorMessages.INCORRECT_LOGIN_REGISTRATION.getErrorMessage()));
            return mapper.map(user, UserDto.class);
        } else {
            checkPhoneNumberError(username);
            User user = userRepository.findByPhoneNumberAndPassword(username, password)
                    .orElseThrow(() -> new UnAuthorizedException(ErrorMessages.INCORRECT_LOGIN_REGISTRATION.getErrorMessage()));
            return mapper.map(user, UserDto.class);
        }
    }

    @Override
    public UserDto updateUser(String userId, @Valid UserDto userDto) {
        ModelMapper mapper = new ModelMapper();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        User userToUpdate = mapper.map(user, User.class);
        userToUpdate.setFirstname(userDto.getFirstname());
        userToUpdate.setLastname(userDto.getLastname());

        User updatedUser = userRepository.save(userToUpdate);
        return mapper.map(updatedUser, UserDto.class);
    }

    @Override
    public UserDto getUser(String userId) {
        ModelMapper mapper = new ModelMapper();

        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        return mapper.map(user, UserDto.class);
    }

    @Override
    public List<UserDto> getUsers(int page, int limit) {
        if (page > 0) page = page - 1;
        Pageable pageable = PageRequest.of(page, limit);
        Page<User> userPage = userRepository.findAll(pageable);
        List<User> users = userPage.getContent();
        Type dtoType = new TypeToken<List<UserDto>>() {
        }.getType();
        return new ModelMapper().map(users, dtoType);
    }

    @Override
    public UserDto deleteUser(String userId) {
        User user = userRepository.findByUserId(userId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));
        userRepository.delete(user);
        return null;
    }

    private void checkEmailError(String email) {
        if (email == null || email.isBlank())
            throw new BadRequestException(ErrorMessages.EMAIL_EMPTY.getErrorMessage());

        if (!utils.isValidEmail(email))
            throw new BadRequestException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
    }

    private void checkPhoneNumberError(String phone) {
        if (phone == null || phone.isBlank())
            throw new BadRequestException(ErrorMessages.PHONE_NUMBER_EMPTY.getErrorMessage());
    }

    private void checkPasswordError(String password) {
        if (password == null || password.isBlank())
            throw new BadRequestException(ErrorMessages.PASSWORD_EMPTY.getErrorMessage());
    }

    private void checkUserDtoError(UserDto userDto) {
        checkEmailError(userDto.getEmail());
        try {
            String domain = userDto.getEmail().substring(userDto.getEmail().lastIndexOf('@') + 1);
            System.out.println("Domain: " + domain);
            int result = utils.doLookup(domain);
            System.out.println("Result of domain: " + result);
        } catch (NamingException e) {
            throw new BadRequestException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
        }
        checkPhoneNumberError(userDto.getPhoneNumber());
        checkPasswordError(userDto.getPassword());

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
        if (user.isPresent())
            throw new BadRequestException(ErrorMessages.RECORD_ALREADY_EXIST.getErrorMessage());
    }
}

