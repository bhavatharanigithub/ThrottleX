package com.throttlex.config;

import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import javax.crypto.SecretKey;

@Configuration
public class JwtConfig {

    @Value("${throttlex.jwt.secret}")
    private String secret;

    @Value("${throttlex.jwt.expiration}")
    private long expirationSeconds;

    @Bean
    public SecretKey jwtSigningKey() {
        // Treat the configured secret as plain text (same style as Node .env)
        return Keys.hmacShaKeyFor(secret.getBytes());
    }

    @Bean
    public SignatureAlgorithm signatureAlgorithm() {
        return SignatureAlgorithm.HS256;
    }

    public long getExpirationSeconds() {
        return expirationSeconds;
    }
}

