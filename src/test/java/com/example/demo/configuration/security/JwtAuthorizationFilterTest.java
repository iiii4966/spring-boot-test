package com.example.demo.configuration.security;

import com.example.demo.domain.model.Account;
import com.example.demo.repository.AccountRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.transaction.annotation.Transactional;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class wJwtAuthorizationFilterTest {

    @Autowired
    JwtAuthorizationFilter jwtAuthorizationFilter;
    @Autowired
    JwtUtil jwtUtil;
    @Autowired
    AccountRepository accountRepository;
    private MockHttpServletRequest mockRequest = new MockHttpServletRequest();
    private MockHttpServletResponse mockResponse = new MockHttpServletResponse();
    private FilterChain mockFilterChain = new MockFilterChain();
    private Account account;
    private String token;
    static class MockFilterChain implements FilterChain {
        @Override
        public void doFilter(ServletRequest request, ServletResponse response)
                throws IOException, ServletException {}
    }


    @BeforeEach
    public void setUp(){
        account = Account.createNew("gapgit", "1234");
        accountRepository.save(account);
        token = jwtUtil.generateAccessToken(account);
        mockRequest = new MockHttpServletRequest();
        mockResponse = new MockHttpServletResponse();
        mockFilterChain = new MockFilterChain();
    }

    @Test
    public void testDoFilterInternalWhenAuth() throws ServletException, IOException {
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, token);
        jwtAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        // then
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertTrue(auth.isAuthenticated());
        Account principal = (Account) auth.getPrincipal();
        assertFalse(principal.isAnonymous());
        assertEquals(principal, account);
    }

    @Test
    public void testDoFilterInternalWhenNotAuth() throws ServletException, IOException {
        // user
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, "");
        jwtAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        // then
        var auth = SecurityContextHolder.getContext().getAuthentication();
        assertFalse(auth.isAuthenticated());

        var principle = (Account) auth.getPrincipal();
        assertTrue(principle.isAnonymous());
        assertEquals(principle.getUsername(), "Anonymous");
        assertFalse(principle.isActive());
        assertFalse(principle.isStaff());
    }

    @Test
    public void testDoFilterInternalNotExistUser() throws ServletException, IOException {
        // user
        accountRepository.deleteOne(account);
        mockRequest.addHeader(HttpHeaders.AUTHORIZATION, token);
        jwtAuthorizationFilter.doFilterInternal(mockRequest, mockResponse, mockFilterChain);
        // then
        var auth = SecurityContextHolder.getContext().getAuthentication();
        var principle = (Account) auth.getPrincipal();
        assertTrue(principle.isAnonymous());
        assertEquals(principle.getUsername(), "Anonymous");
        assertFalse(principle.isActive());
        assertFalse(principle.isStaff());
    }
}