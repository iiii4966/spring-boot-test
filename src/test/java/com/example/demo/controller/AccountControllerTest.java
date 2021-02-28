package com.example.demo.controller;

import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.dto.UserView;
import com.example.demo.exception.AlreadyExistEntityException;
import com.example.demo.exception.ErrorResponse;
import com.example.demo.repository.AccountRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static utils.JsonHelper.fromJson;
import static utils.JsonHelper.toJson;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
class AccountControllerTest {

    @Autowired
    MockMvc mockMvc;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void SignupSuccess() throws Exception {
        // given
        var request = new CreateUserRequest();
        request.setUsername("gapgit@gmail.com");
        request.setPassword("1234");

        var userView = new UserView();
        userView.setUsername("gapgit@gmail.com");
        //when
        var response = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();
        System.out.println(response.getResponse().getContentAsString());
        var resultUserView = fromJson(objectMapper, response.getResponse().getContentAsString(), UserView.class);
        assertEquals(userView.getUsername(), resultUserView.getUsername());
        assertNotNull(accountRepository.findOne(resultUserView.getId()));
    }

    @Test
    public void SignupFailAsUserNameDuplicated() throws Exception {
        // given
        var request = new CreateUserRequest();
        request.setUsername("gapgit@gmail.com");
        request.setPassword("1234");

        var errResponse = new ErrorResponse();
        errResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errResponse.setMessage(new AlreadyExistEntityException().getMessage() + "[username]");

        //when
        mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
                .andExpect(status().isOk())
                .andExpect(header().exists(HttpHeaders.AUTHORIZATION))
                .andReturn();

        var response = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
        // then
        System.out.println(response.getResponse().getContentAsString());
        var resultErrResponse = fromJson(objectMapper, response.getResponse().getContentAsString(), ErrorResponse.class);
        assertEquals(errResponse.getMessage(), resultErrResponse.getMessage());
        assertEquals(errResponse.getStatus(), resultErrResponse.getStatus());
    }

    @Test
    public void SignupFailAsInvalidRequestUserName() throws Exception {
        // given
        var request = new CreateUserRequest();
        request.setUsername("gapgit/gmail.com");
        request.setPassword("1234");

        var errResponse = new ErrorResponse();
        errResponse.setStatus(HttpStatus.UNPROCESSABLE_ENTITY.value());
        errResponse.setMessage("Validation error. Check 'errors' field for details.");
        errResponse.addValidationError("username", "must be a well-formed email address");

        //when
        var response = mockMvc.perform(post("/users/signup")
                .contentType(MediaType.APPLICATION_JSON)
                .content(toJson(objectMapper, request)))
                .andExpect(status().isUnprocessableEntity())
                .andExpect(header().doesNotExist(HttpHeaders.AUTHORIZATION))
                .andReturn();
        // then
        var resultErrResponse = fromJson(objectMapper, response.getResponse().getContentAsString(), ErrorResponse.class);
        assertEquals(errResponse.getMessage(), resultErrResponse.getMessage());
        assertEquals(errResponse.getStatus(), resultErrResponse.getStatus());
        assertEquals(errResponse.getStatus(), resultErrResponse.getStatus());
    }

}