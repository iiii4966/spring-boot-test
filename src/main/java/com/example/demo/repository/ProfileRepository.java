package com.example.demo.repository;

import com.example.demo.domain.model.Profile;
import com.example.demo.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;

@Repository
@RequiredArgsConstructor
public class ProfileRepository {

    private final EntityManager em;

    public Long create(Profile profile, Account account) {
        em.persist(profile);
        profile.setAccount(account);
        return profile.getId();
    }

    public Profile findById(Long id) {
        return em.find(Profile.class, id);
    }
}
