package com.agentathreya.conversationservice.service;

import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.agentathreya.conversationservice.dto.AuthRequest;
import com.agentathreya.conversationservice.entity.Role;
import com.agentathreya.conversationservice.entity.RoleType;
import com.agentathreya.conversationservice.entity.User;
import com.agentathreya.conversationservice.repository.RoleRepository;
import com.agentathreya.conversationservice.repository.UserRepository;
import com.agentathreya.conversationservice.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthService {
    
	@Autowired
    private UserRepository userRepository;
	@Autowired
    private RoleRepository roleRepository;
	@Autowired
    private PasswordEncoder passwordEncoder;
	@Autowired
    private JwtService jwtService;
	@Autowired
    private AuthenticationManager authenticationManager;

    /**
     * **Registers a new user** with a default ROLE_USER.
     */
    public String register(AuthRequest authRequest) {
        if (userRepository.existsByUsername(authRequest.getUsername())) {
            throw new RuntimeException("Username already taken!");
        }

        // Get the default ROLE_USER (or create it if not exists)
        Role userRole = roleRepository.findByName(RoleType.ROLE_USER)
                .orElseThrow(() -> new RuntimeException("Default Role not found!"));

        // Create new user
        User user = new User();
        user.setUsername(authRequest.getUsername());
        user.setPassword(passwordEncoder.encode(authRequest.getPassword()));
        user.setRoles(Collections.singleton(userRole));

        // Save user
        userRepository.save(user);

        // Generate JWT Token
        return jwtService.generateToken(authRequest.getUsername());
    }

    /**
     * **Logs in the user and returns a JWT token.**
     */
    public String login(AuthRequest authRequest) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(authRequest.getUsername(), authRequest.getPassword())
        );

        // Generate JWT Token
        return jwtService.generateToken(authRequest.getUsername());
    }
}
