package com.example.demo.configuration.security;

import com.example.demo.domain.dto.CreateUserRequest;
import com.example.demo.domain.model.Account;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.http.HttpHeaders;
import org.springframework.security.authentication.AuthenticationServiceException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

import static org.springframework.http.MediaType.APPLICATION_JSON;


@Component
public class JwtAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
    private final JwtUtil jwtUtil;

    public JwtAuthenticationFilter(AuthenticationManagerImpl authenticationManager, JwtUtil jwtUtil) {
        super(authenticationManager);
        this.jwtUtil = jwtUtil;
    }

    @Override
    public Authentication attemptAuthentication(HttpServletRequest request, HttpServletResponse response)
            throws AuthenticationException {
        if (!request.getMethod().equals("POST")) {
            throw new AuthenticationServiceException("Authentication method not supported: " + request.getMethod());
        }

        if (request.getContentType().equals(APPLICATION_JSON.toString())) {
            return attemptAuthenticationAsJson(request, response);
        }
        return super.attemptAuthentication(request, response);
    }

    public Authentication attemptAuthenticationAsJson(HttpServletRequest request, HttpServletResponse response) {
        String username = "";
        String password = "";
        try {
            var mapper = new ObjectMapper();
            CreateUserRequest dto = mapper.readValue(request.getInputStream(), CreateUserRequest.class);
            username = dto.getUsername();
            password = dto.getPassword();
        } catch (IOException ignored){}

        UsernamePasswordAuthenticationToken authRequest = new UsernamePasswordAuthenticationToken(username, password);
        setDetails(request, authRequest);
        return this.getAuthenticationManager().authenticate(authRequest);
    }

    @Override
    protected void successfulAuthentication(HttpServletRequest req,
                                            HttpServletResponse res,
                                            FilterChain chain,
                                            Authentication auth) throws IOException, ServletException {
        var account = (Account) auth.getPrincipal();
        res.addHeader(HttpHeaders.AUTHORIZATION, jwtUtil.generateAccessToken(account.getId()));
    }
}
