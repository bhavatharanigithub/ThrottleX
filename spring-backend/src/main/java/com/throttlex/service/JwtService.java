package com.throttlex.service;

import com.throttlex.config.JwtConfig;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.IOException;
import org.springframework.stereotype.Service;

import javax.crypto.SecretKey;
import java.time.Instant;
import java.util.Date;
import java.util.Map;

@Service
public class JwtService {

    private final SecretKey signingKey;
    private final SignatureAlgorithm algorithm;
    private final JwtConfig config;

    public JwtService(SecretKey signingKey, SignatureAlgorithm algorithm, JwtConfig config) {
        this.signingKey = signingKey;
        this.algorithm = algorithm;
        this.config = config;
    }

    public String generateToken(String userId, String role) {
        Instant now = Instant.now();
        Instant expiry = now.plusSeconds(config.getExpirationSeconds());

        return Jwts.builder()
                .subject(userId)
                .claim("role", role)
                .issuedAt(Date.from(now))
                .expiration(Date.from(expiry))
                .signWith(signingKey, algorithm)
                .compact();
    }

    public Map<String, Object> validateAndExtract(String token) {
        var claimsJws = Jwts.parser()
                .verifyWith(signingKey)
                .build()
                .parseSignedClaims(token);

        return claimsJws.getPayload();
    }
}

