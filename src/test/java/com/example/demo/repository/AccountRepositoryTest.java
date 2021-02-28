package com.example.demo.repository;

import com.example.demo.domain.model.Account;
import org.junit.jupiter.api.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;


@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
class AccountRepositoryTest {

    @Autowired
    AccountRepository accountRepository;

    @Test
    public void testUser() throws Exception{
        // given
        var user = Account.createNew("gapgit", "1234");

        // when
        Long id = accountRepository.save(user);
        var findUser = accountRepository.findOne(id);

        // then
        assertSame(id, user.getId());
        assertSame(user.getUsername(), findUser.getUsername());
        assertSame(user.getPassword(), findUser.getPassword());
    }

    @Test
    public void testExistByUsernameTrue() {
        // given
        var user = Account.createNew("gapgit", "1234");
        Long id = accountRepository.save(user);
        // when
        var result = accountRepository.exist("gapgit");
        // then
        assertTrue(result);
    }

    @Test
    public void testExistByUsernameFalse() {
        // when
        var result = accountRepository.exist("gapgit");
        // then
        assertFalse(result);
    }
}