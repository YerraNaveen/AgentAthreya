package com.agentathreya.conversationservice.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.agentathreya.conversationservice.dto.AuthRequest;
import com.agentathreya.conversationservice.service.AuthService;
import com.agentathreya.conversationservice.security.JwtService;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/auth")
@RequiredArgsConstructor
public class AuthController {
    @Autowired
    private AuthService authService;

    @Autowired
    private JwtService jwtService;

    /**
     * **Registers a new user and returns a JWT token.**
     */
    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody AuthRequest authRequest) {
        String token = authService.register(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * **Authenticates the user and returns a JWT token.**
     */
    @PostMapping("/login")
    public ResponseEntity<String> login(@RequestBody AuthRequest authRequest) {
        String token = authService.login(authRequest);
        return ResponseEntity.ok(token);
    }

    /**
     * **Refreshes the JWT token.**
     */
    @PostMapping("/refresh-token")
    public ResponseEntity<String> refreshToken(@RequestBody String token) {
        try {
            String refreshedToken = jwtService.refreshToken(token);
            return ResponseEntity.ok(refreshedToken);
        } catch (Exception e) {
            return ResponseEntity.status(400).body("Invalid token");
        }
    }
}
