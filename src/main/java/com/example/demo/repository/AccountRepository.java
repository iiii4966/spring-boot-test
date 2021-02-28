package com.example.demo.repository;

import com.example.demo.domain.model.Account;
import lombok.RequiredArgsConstructor;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.EntityManager;
import javax.persistence.NoResultException;
import java.util.List;

@Repository
@RequiredArgsConstructor
public class AccountRepository {

    private final EntityManager em;

    public Long save(Account account) {
        em.persist(account);
        return account.getId();
    }

    public boolean deleteOne(Long id) {
        var query = em.createQuery("DELETE FROM auth_user WHERE id = ?1")
                .setParameter(1, id);
        return query.executeUpdate() == 1;
    }

    public boolean deleteOne(Account account) {
        try {
            em.remove(account);
            return true;
        } catch (IllegalArgumentException e) {
            return false;
        }
    }

    public Account findOne(Long id) {
        return em.find(Account.class, id);
    }

    public List<Account> findOne(@Param("username") String username) {
        return em.createQuery("SELECT a FROM Account a WHERE a.username = ?1", Account.class)
                .setParameter(1, username).getResultList();
    }

    public boolean exist(@Param("username") String username) {
        try {
            em.createQuery("SELECT a.id FROM Account a WHERE a.username = ?1", Long.class)
                    .setParameter(1, username).getSingleResult();
            return true;
        } catch (NoResultException e) {
            return false;
        }
    }

}
