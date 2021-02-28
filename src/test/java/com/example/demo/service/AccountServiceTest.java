package com.example.demo.service;

import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountServiceTest {

    @Autowired UserService userService;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void register() {
        // given
        String username = "gapgit@gmail.com";
        String password = "1234";
        // when
        var account = userService.register(username, password);
        // then
        var findAccount = accountRepository.findOne(account.getId());
        assertEquals(account.getId(), findAccount.getId());
    }
}