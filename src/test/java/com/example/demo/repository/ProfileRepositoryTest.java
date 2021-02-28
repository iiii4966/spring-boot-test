package com.example.demo.repository;

import com.example.demo.domain.model.Account;
import com.example.demo.domain.model.Profile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ProfileRepositoryTest {

    @Autowired ProfileRepository profileRepository;

    final String nickName = "gapgit";

    @Test
    Long create() {
        var user = Account.createNew(nickName, "1234");
        var profile = Profile.createNew(nickName, user);
        return profileRepository.create(profile, user);
    }

    @Test
    void findById() {
        Long id = this.create();
        var profile = profileRepository.findById(id);
        assertSame(profile.getNick(), nickName);
        assertSame(profile.getId(), id);
        assertSame(profile.getAccount().getUsername(), this.nickName);
    }
}