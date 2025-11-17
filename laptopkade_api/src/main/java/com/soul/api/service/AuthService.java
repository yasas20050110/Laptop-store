package com.soul.api.service;

import com.soul.api.dto.JwtResponse;
import com.soul.api.dto.LoginRequest;
import com.soul.api.dto.SignupRequest;
import com.soul.api.exception.ResourceNotFoundException;
import com.soul.api.model.Token;
import com.soul.api.model.User;
import com.soul.api.repository.TokenRepository;
import com.soul.api.repository.UserRepository;
import com.soul.api.security.JwtTokenProvider;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Date;

@Service
@Transactional
public class AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TokenRepository tokenRepository;

    @Autowired
    private JwtTokenProvider jwtTokenProvider;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Register a new user
     */
    public User registerUser(SignupRequest signupRequest) {
        // Validate input
        if (signupRequest.getUsername() == null || signupRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (signupRequest.getEmail() == null || signupRequest.getEmail().trim().isEmpty()) {
            throw new IllegalArgumentException("Email cannot be empty");
        }
        if (signupRequest.getPassword() == null || signupRequest.getPassword().length() < 6) {
            throw new IllegalArgumentException("Password must be at least 6 characters");
        }
        if (!signupRequest.getPassword().equals(signupRequest.getConfirmPassword())) {
            throw new IllegalArgumentException("Passwords do not match");
        }

        // Check if user already exists
        if (userRepository.existsByUsername(signupRequest.getUsername())) {
            throw new IllegalArgumentException("Username already exists");
        }
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
            throw new IllegalArgumentException("Email already registered");
        }

        // Create new user
        User user = new User();
        user.setUsername(signupRequest.getUsername());
        user.setEmail(signupRequest.getEmail());
        user.setPassword(passwordEncoder.encode(signupRequest.getPassword()));
        user.setEnabled(true);
        user.setCreatedAt(LocalDateTime.now().toString());

        return userRepository.save(user);
    }

    /**
     * Login user and return JWT token
     */
    public JwtResponse loginUser(LoginRequest loginRequest) {
        // Validate input
        if (loginRequest.getUsername() == null || loginRequest.getUsername().trim().isEmpty()) {
            throw new IllegalArgumentException("Username cannot be empty");
        }
        if (loginRequest.getPassword() == null || loginRequest.getPassword().trim().isEmpty()) {
            throw new IllegalArgumentException("Password cannot be empty");
        }

        // Find user
        User user = userRepository.findByUsername(loginRequest.getUsername())
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", loginRequest.getUsername()));

        // Validate password
        if (!passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
            throw new IllegalArgumentException("Invalid credentials");
        }

        if (!user.getEnabled()) {
            throw new IllegalArgumentException("User account is disabled");
        }

        // Generate JWT token
        String token = jwtTokenProvider.generateToken(user.getUsername(), user.getEmail());
        long expirationTime = jwtTokenProvider.getExpirationTime();

        // Save token to database
        Token tokenEntity = new Token();
        tokenEntity.setTokenValue(token);
        tokenEntity.setUser(user);
        tokenEntity.setTokenType("Bearer");
        tokenEntity.setRevoked(false);
        
        // Convert expiration time to LocalDateTime
        LocalDateTime expiresAt = new Date(System.currentTimeMillis() + expirationTime)
                .toInstant()
                .atZone(ZoneId.systemDefault())
                .toLocalDateTime();
        tokenEntity.setExpiresAt(expiresAt);
        tokenEntity.setIssuedAt(LocalDateTime.now());
        
        tokenRepository.save(tokenEntity);

        return new JwtResponse(token, expirationTime, user.getUsername(), user.getEmail());
    }

    /**
     * Get user by username
     */
    @Transactional(readOnly = true)
    public User getUserByUsername(String username) {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> new ResourceNotFoundException("User", "username", username));
    }

    /**
     * Validate token
     */
    @Transactional(readOnly = true)
    public boolean validateToken(String token) {
        return jwtTokenProvider.validateToken(token);
    }

    /**
     * Revoke a token
     */
    public void revokeToken(String token) {
        Token tokenEntity = tokenRepository.findByTokenValue(token)
                .orElseThrow(() -> new ResourceNotFoundException("Token", "value", token));
        tokenEntity.setRevoked(true);
        tokenRepository.save(tokenEntity);
    }

    /**
     * Get all active tokens for a user
     */
    @Transactional(readOnly = true)
    public java.util.List<Token> getUserActiveTokens(Long userId) {
        return tokenRepository.findByUserIdAndRevokedFalse(userId);
    }
}
