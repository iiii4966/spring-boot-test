package com.example.demo.domain.mapper;

import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class AccountMapperTest {

    @Autowired UserMapper userMapper;
    @Autowired
    AccountRepository accountRepository;

    @Test
    public void userToCreateUserRequest() {
        // given
        var user = Account.createNew("gapgit", "1234");
        // when
        CreateUserRequest dto = userMapper.ToCreateUserRequest(user);
        // then
        assertFalse(dto.getUsername().isBlank());
        assertFalse(dto.getPassword().isBlank());
    }

    @Test
    public void CreateUserRequestToUser() {
        // given
        var dto = new CreateUserRequest();
        dto.setUsername("gapgit");
        dto.setPassword("1234");
        // when
        Account account = userMapper.createUserRequestToUser(dto);
        // then
        assertFalse(account.getUsername().isBlank());
        assertFalse(account.getPassword().isBlank());
    }

    @Test
    public void userToUserView() {
        // given
        var user = Account.createNew("gapgit", "1234");
        accountRepository.save(user);
        // when
        var userView = userMapper.toUserView(user);
        // then
        assertNotNull(userView.getId());
        assertFalse(userView.getUsername().isBlank());
    }
}