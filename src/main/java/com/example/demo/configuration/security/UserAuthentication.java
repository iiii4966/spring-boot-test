package com.example.demo.configuration.security;

import com.example.demo.domain.model.Account;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.util.Assert;

import java.util.Collection;


public class UserAuthentication implements Authentication {

    private final Collection<? extends GrantedAuthority> authorities;
    private final Account principal;
    private boolean isAuthenticated = true;

    public UserAuthentication(Account principal, Collection<? extends GrantedAuthority> authorities) {
        this.principal = principal;
        setAuthenticated(!principal.isAnonymous());

        if (authorities.isEmpty()) {
            this.authorities = AuthorityUtils.NO_AUTHORITIES;
        } else {
            for (GrantedAuthority a : authorities) {
                Assert.notNull(a, "Authorities collection cannot contain any null elements");
            }
            this.authorities = authorities;
        }
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return this.authorities;
    }

    @Override
    public Object getPrincipal() {
        return this.principal;
    }

    @Override
    public boolean isAuthenticated() {
        return isAuthenticated;
    }

    @Override
    public void setAuthenticated(boolean isAuthenticated) throws IllegalArgumentException {
        this.isAuthenticated = isAuthenticated;
    }

    // not used
    private Object credentials;

    // not used
    @Override
    public Object getCredentials() {
        return this.credentials;
    }

    // not used
    @Override
    public Object getDetails() {
        return null;
    }

    // not used
    @Override
    public String getName() {
        return null;
    }
}
