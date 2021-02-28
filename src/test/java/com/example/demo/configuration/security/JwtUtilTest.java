package com.example.demo.configuration.security;

import com.example.demo.domain.model.Account;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class JwtUtilTest {

    @Autowired
    JwtUtil jwtUtil;
    Account account;

    @BeforeEach
    public void setUp() {
        account = Account.createNew("gapgit", "1234");
    }

    @Test
    void decode() {
        // given
        var token = jwtUtil.generateAccessToken(account);
        // when
        var decoded = jwtUtil.decode(token);
        // then
        assertEquals(token.split(" ")[1], decoded.get().getToken());
    }

    @Test
    void getSubject() {
        // given
        var token = jwtUtil.generateAccessToken(account);
        // when
        var decoded = jwtUtil.decode(token);
        // then
        assertTrue(decoded.isPresent());
        assertEquals(decoded.get().getSubject(), account.getId().toString());
    }

    @Test
    public void generateAccessTokenWithUserId() {
        // given
        var token = jwtUtil.generateAccessToken(account.getId());
        // then
        assertEquals(token.length(), 112);
    }

    @Test
    public void generateAccessTokenWithUser() {
        // given
        var token = jwtUtil.generateAccessToken(account);
        // then
        assertEquals(token.length(), 112);
    }
}