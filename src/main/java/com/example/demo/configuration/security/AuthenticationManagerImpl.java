package com.example.demo.configuration.security;

import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationCredentialsNotFoundException;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;
import java.util.Collections;
import java.util.List;

@Component
@RequiredArgsConstructor
public class AuthenticationManagerImpl implements AuthenticationManager {

    private final AccountRepository accountRepository;
    private final BCryptPasswordEncoder passwordEncoder;

    @Override
    public Authentication authenticate(Authentication authentication) throws AuthenticationException {
        String username = (String) authentication.getPrincipal();
        String rawPassword = (String) authentication.getCredentials();

        List<Account> account = accountRepository.findOne(username);
        if (account.isEmpty())
            throw new UsernameNotFoundException("INVALID USERNAME");

        var findAccount = account.stream().findFirst().get();
        if (!passwordEncoder.matches(rawPassword, findAccount.getPassword()))
            throw new AuthenticationCredentialsNotFoundException("INVALID PASSWORD");
        return new UserAuthentication(findAccount, Collections.emptyList());
    }
}
