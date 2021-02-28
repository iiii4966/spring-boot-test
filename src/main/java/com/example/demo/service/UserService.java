package com.example.demo.service;

import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Transactional
    public Account register (String username, String password){
        var account = Account.createNew(username, passwordEncoder.encode(password));
        this.accountRepository.save(account);
        return account;
    }

    public boolean checkDuplicate(String username) {
        return accountRepository.exist(username);
    }

    public Account getAccountInfo(Long userId) {
        return accountRepository.findOne(userId);
    }
}
