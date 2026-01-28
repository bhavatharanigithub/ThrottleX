package com.throttlex.controller;

import com.throttlex.service.JwtService;
import com.throttlex.service.RateLimiterService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class ProtectedController {

    private final JwtService jwtService;
    private final RateLimiterService rateLimiterService;

    public ProtectedController(JwtService jwtService, RateLimiterService rateLimiterService) {
        this.jwtService = jwtService;
        this.rateLimiterService = rateLimiterService;
    }

    @GetMapping("/protected")
    public ResponseEntity<?> protectedEndpoint(HttpServletRequest request) {
        String authHeader = request.getHeader("Authorization");
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Missing or invalid Authorization header"));
        }

        String token = authHeader.substring(7);
        Map<String, Object> claims;
        try {
            claims = jwtService.validateAndExtract(token);
        } catch (Exception ex) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(Map.of("error", "Invalid or expired token"));
        }

        String subject = (String) claims.get("sub");
        String role = (String) claims.getOrDefault("role", "free");

        String rateKey = subject != null ? subject : request.getRemoteAddr();
        if (!rateLimiterService.isAllowed(rateKey)) {
            return ResponseEntity.status(HttpStatus.TOO_MANY_REQUESTS)
                    .body(Map.of("error", "Rate limit exceeded"));
        }

        return ResponseEntity.ok(
                Map.of(
                        "message", "Access granted",
                        "userId", subject,
                        "role", role
                )
        );
    }
}

