package com.example.demo.configuration.security;

import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpHeaders;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;

import static java.util.Collections.emptyList;

@Component
@RequiredArgsConstructor
public class JwtAuthorizationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final AccountRepository accountRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain) throws ServletException, IOException {
        final String token = request.getHeader(HttpHeaders.AUTHORIZATION);

        Account account;
        if (Objects.isNull(token)) {
            account = Account.createAnonymous();
        } else {
            final var decoded = jwtUtil.decode(token);
            if (decoded.isPresent()) {
                account = accountRepository.findOne(Long.parseLong(decoded.get().getSubject()));
            } else {
                account = Account.createAnonymous();
            }
        }

        SecurityContextHolder.getContext().setAuthentication(new UserAuthentication(account, emptyList()));
        chain.doFilter(request, response);
    }

}
