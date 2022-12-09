package com.example.blog.controller;


import com.example.blog.service.UserService;
import com.example.blog.shared.dto.UserDto;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

/**
 * @author Ikechi Ucheagwu
 * @created 07/12/2022 - 02:19
 * @project Blog
 */


/**
 * 1. Annotate the class with @RunWith(SpringRunner.class) - JUnit4
 * 2. Annotate the class with @WebMvcTest
 * 3. Inject WebApplicationContext
 * 4. Mock the dependency injected in the class under test
 * 5. Create a global instance of MockMvc
 * 6. Open Mock before each method under test.
 */

@RunWith(SpringRunner.class)
@WebMvcTest(UserController.class)
@WebAppConfiguration
class UserControllerTest {

    @Autowired
    WebApplicationContext webApplicationContext;

    @MockBean
    UserService mockUserService;

    MockMvc mockMvc;
    UserDto userDto;


    @BeforeEach
    void init() {
        MockitoAnnotations.openMocks(this);
        this.mockMvc = MockMvcBuilders
                .webAppContextSetup(this.webApplicationContext)
                .build();

        userDto = new UserDto();
        userDto.setUserId("1234454");
        userDto.setFirstname("Wale");
        userDto.setEmail("wale@gmail.com");
        userDto.setPhoneNumber("+2348147428543");
        userDto.setPassword("123456");
    }

    @Test
    void createUser() throws Exception {
        when(mockUserService.createUser(any(UserDto.class)))
                .thenReturn(userDto);

        String jsonUserRequest = "{\"firstname\": \"Wale\", \"lastname\": \"Wale\", \"email\": \"wale@gmail.com\", \"password\": \"123456\","
                + "\"phoneNumber\": \"+2348147428543\"}";
        String urlPath = "users/register/";
        MediaType contentType = new MediaType("application", "json");

        MvcResult mvcResult = mockMvc.perform(
                        post(urlPath)
                                .contentType(contentType)
                                .content(jsonUserRequest))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.success").value(true))
                .andExpect(MockMvcResultMatchers.jsonPath("$.data.firstname").value("Wale"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.status").value(HttpStatus.CREATED))
                .andReturn();

        assertEquals(MediaType.APPLICATION_JSON_VALUE, mvcResult.getResponse().getContentType());
        verify(mockUserService, times(1)).createUser(any(UserDto.class));
    }

}