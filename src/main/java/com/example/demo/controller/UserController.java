package com.example.demo.controller;

import com.example.demo.configuration.security.JwtUtil;
import com.example.demo.domain.model.Account;
import com.example.demo.exception.AlreadyExistEntityException;
import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.dto.UserView;
import com.example.demo.domain.mapper.UserMapper;
import com.example.demo.repository.AccountRepository;
import com.example.demo.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;

import javax.validation.Valid;
import java.security.Principal;
import java.util.NoSuchElementException;
import java.util.Objects;

@Controller
@RequestMapping(path = "users")
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;
    private final AccountRepository accountRepository;
    private final UserMapper userMapper;
    private final JwtUtil jwtUtil;

    private ResponseEntity<UserView> signup(CreateUserRequest dto){
        if (userService.checkDuplicate(dto.getUsername())) {
            throw new AlreadyExistEntityException("username");
        }
        Account account = userService.register(dto.getUsername(), dto.getPassword());
        String token = jwtUtil.generateAccessToken(account.getId());
        return ResponseEntity.ok().header(HttpHeaders.AUTHORIZATION, token)
                .body(userMapper.toUserView(account));
    }

    @PostMapping(path = "/signup")
    public ResponseEntity<UserView> signupWithJson(@RequestBody @Valid CreateUserRequest dtoJson) {
        return signup(dtoJson);
    }

    @PostMapping(
            path = "/signup",
            consumes = {MediaType.APPLICATION_FORM_URLENCODED_VALUE}
    )
    public ResponseEntity<UserView> signupWithForm(@ModelAttribute @Valid CreateUserRequest dtoForm) {
        return signup(dtoForm);
    }

    @GetMapping("/{userId}")
    public ResponseEntity<UserView> getUser(@PathVariable Long userId) {
        Account account = userService.getAccountInfo(userId);
        if (Objects.isNull(account)) {
            throw new NoSuchElementException("Not Found User" + userId);
        }
        return ResponseEntity.ok().body(userMapper.toUserView(account));
    }
}
