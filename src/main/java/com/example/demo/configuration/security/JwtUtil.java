package com.example.demo.configuration.security;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTVerifier;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.example.demo.domain.model.Account;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.Objects;
import java.util.Optional;


@Component
public class JwtUtil {

    @Value("${security.jwt.secret-key:secret-key}")
    private String JWT_SECRET;
    private final ChronoUnit EXPIRATION_TIME = ChronoUnit.WEEKS;

    private JWTVerifier verifier;
    private Algorithm algorithm;

    private Algorithm algorithm(){
        if (algorithm == null) {
            algorithm = Algorithm.HMAC256(JWT_SECRET);
        }
        return algorithm;
    }

    private JWTVerifier verifier(){
        if (verifier == null) {
            verifier = JWT.require(algorithm()).build();
        }
        return verifier;
    }

    private LocalDate getExpiredAt(){
        return LocalDate.now().plus(1, EXPIRATION_TIME);
    }

    public String generateAccessToken(Long userId) {
        Objects.requireNonNull(userId);
        String body = JWT.create()
                .withSubject(userId.toString())
                .withExpiresAt(java.sql.Date.valueOf(getExpiredAt()))
                .sign(Algorithm.HMAC256(JWT_SECRET));
        return "Bearer " + body;
    }

    public String generateAccessToken(Account account) {
        Objects.requireNonNull(account.getId());
        return generateAccessToken(account.getId());
    }

    public Optional<DecodedJWT> decode(String token) {
        if (token == null || token.isEmpty() || token.isBlank()) return Optional.empty();
        var parts = token.split(" ");
        if (parts.length != 2) return Optional.empty();
        try {
            var decoded = verifier().verify(parts[1]);
            return Optional.of(decoded);
        } catch (JWTVerificationException exception) {
            return Optional.empty();
        }
    }
}
