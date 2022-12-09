package com.example.blog.service.impl;

import com.example.blog.entities.Role;
import com.example.blog.entities.User;
import com.example.blog.exception.BadRequestException;
import com.example.blog.exception.ErrorMessages;
import com.example.blog.repositories.RoleRepository;
import com.example.blog.repositories.UserRepository;
import com.example.blog.security.JwtUtils;
import com.example.blog.service.UserService;
import com.example.blog.shared.dto.UserDto;
import com.example.blog.shared.utils.Roles;
import com.example.blog.shared.utils.Utils;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeToken;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.naming.NamingException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@AllArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    private final Utils utils;

    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private final JwtUtils jwtUtils;
    private final AuthenticationManager authenticationManager;

    private final RoleRepository roleRepository;

    @Override
    public UserDto createUser(UserDto userDto) {
        User userToCreate = getUserToCreate(userDto);
        /*
          Set up roles
          user have default role (ROLE_USER)
        */
        Collection<Role> userRoles = getUserRoles(null);
        userToCreate.setRoles(userRoles);

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
        return new ModelMapper().map(createdUser, UserDto.class);
    }

    /* Create user by an admin */
    @Override
    public UserDto createUserByAdmin(String adminId, UserDto userDto) {
        User userToCreate = getUserToCreate(userDto);

        User adminUser = userRepository.findByUserId(adminId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.ACCESS_DENIED.getErrorMessage()));

        checkForAdminError(adminUser);

        Collection<Role> userRoles = getUserRoles(userDto.getRoles());

        userToCreate.setRoles(userRoles);

        User createdUser = userRepository.save(userToCreate);

        return new ModelMapper().map(createdUser, UserDto.class);
    }

    @Override
    public UserDto authenticate(UserDto userDto) {
        final User user = userRepository.findByEmailOrPhoneNumber(userDto.getUsername(), userDto.getUsername())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.INCORRECT_LOGIN_CREDENTIALS.getErrorMessage()));

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(user.getUsername(), userDto.getPassword())
        );

        final String jwt = jwtUtils.generateToken(user);
        final String userId = user.getUserId();

        UserDto returnValue = new UserDto();
        returnValue.setToken("Bearer " + jwt);
        returnValue.setUserId(userId);
        List<String> roles = user.getRoles().stream().map(Role::getName).toList();
        returnValue.setRoles(roles);
        return returnValue;
    }

    @Override
    public UserDto updateUser(String userId, UserDto userDto) {
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
    public UserDto updateUserByAdmin(String adminId, UserDto userDto) {
        User adminUser = userRepository.findByUserId(adminId)
                .orElseThrow(() -> new BadRequestException(ErrorMessages.FORBIDDEN.getErrorMessage()));

        checkForAdminError(adminUser);

        ModelMapper mapper = new ModelMapper();

        User user = userRepository.findByUserId(userDto.getUserId())
                .orElseThrow(() -> new BadRequestException(ErrorMessages.NO_RECORD_FOUND.getErrorMessage()));

        User userToUpdate = mapper.map(user, User.class);
        userToUpdate.setFirstname(userDto.getFirstname());
        userToUpdate.setLastname(userDto.getLastname());

        Collection<Role> userRoles = getUserRoles(userDto.getRoles());

        userToUpdate.setRoles(userRoles);

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
        Collection<Role> roles = user.getRoles();
        boolean status = roles.stream().anyMatch(role -> role.getName().equals(Roles.ROLE_SUPER_ADMIN.name()));

        if (!status)
            userRepository.delete(user);
        else
            throw new BadRequestException(ErrorMessages.ACCESS_DENIED.getErrorMessage());
        return null;
    }

    private User getUserToCreate(UserDto userDto) {
        checkEmailAndExistingUser(userDto);
        User userToCreate = new ModelMapper().map(userDto, User.class);
        String userId = utils.generateUserId(10);
        userToCreate.setUserId(userId);
        userToCreate.setEncryptedPassword(bCryptPasswordEncoder.encode(userDto.getPassword()));
        userToCreate.setEmailVerificationToken(jwtUtils.generateEmailVerificationToken(userId));
        return userToCreate;
    }

    private void checkForAdminError(User user) {
        Collection<Role> roles = user.getRoles();
        boolean isAdmin = false;

        boolean status = roles.stream().anyMatch(role -> role.getName().equals(Roles.ROLE_SUPER_ADMIN.name()));
        if (status)
            isAdmin = true;

        if (!isAdmin)
            throw new BadRequestException(ErrorMessages.ACCESS_DENIED.getErrorMessage());
    }

    private Collection<Role> getUserRoles(Collection<String> roleNames) {
        Collection<Role> roles = new HashSet<>();

        if (roleNames == null || roleNames.isEmpty()) {
            roles.add(roleRepository.findByName(Roles.ROLE_USER.name()));
            return roles;
        }

        for (String roleName : roleNames) {
            Role role = roleRepository.findByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }

        if (roles.stream().anyMatch(role -> role.getName().equals(Roles.ROLE_SUPER_ADMIN.name())))
            throw new BadRequestException(ErrorMessages.ACCESS_DENIED.getErrorMessage());

        return roles;
    }

    private void checkEmailAndExistingUser(UserDto userDto) {
        boolean validEmail = utils.isValidEmail(userDto.getEmail());
        if(!validEmail)
            throw new BadRequestException(ErrorMessages.INVALID_EMAIL.getErrorMessage());

        try {
            String domain = userDto.getEmail().substring(userDto.getEmail().lastIndexOf('@') + 1);
            System.out.println("Domain: " + domain);
            int result = utils.doLookup(domain);
            System.out.println("Result of domain: " + result);
        } catch (NamingException e) {
            throw new BadRequestException(ErrorMessages.INVALID_EMAIL.getErrorMessage());
        }

        Optional<User> user = userRepository.findByEmailOrPhoneNumber(userDto.getEmail(), userDto.getPhoneNumber());
        if (user.isPresent())
            throw new BadRequestException(ErrorMessages.RECORD_ALREADY_EXIST.getErrorMessage());
    }

}

