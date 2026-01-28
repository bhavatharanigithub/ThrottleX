package com.throttlex.controller;

import com.throttlex.service.JwtService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api")
public class AuthController {

    private final JwtService jwtService;

    public AuthController(JwtService jwtService) {
        this.jwtService = jwtService;
    }

    @GetMapping
    public Map<String, String> root() {
        return Map.of("message", "ThrottleX Spring API Gateway is running");
    }

    @GetMapping("/generate-token")
    public ResponseEntity<Map<String, String>> generateToken(
            @RequestParam(name = "role", defaultValue = "free") String role
    ) {
        String token = jwtService.generateToken("user123", role);
        return ResponseEntity.ok(Map.of("token", token));
    }
}

