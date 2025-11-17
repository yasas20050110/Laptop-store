package com.soul.api.controller;

import com.soul.api.dto.JwtResponse;
import com.soul.api.dto.LoginRequest;
import com.soul.api.dto.SignupRequest;
import com.soul.api.model.Token;
import com.soul.api.model.User;
import com.soul.api.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/auth")
@CrossOrigin(origins = "*", maxAge = 3600)
public class AuthController {

    @Autowired
    private AuthService authService;

    /**
     * Register a new user
     * POST /api/auth/signup
     */
    @PostMapping("/signup")
    public ResponseEntity<User> registerUser(@RequestBody SignupRequest signupRequest) {
        User newUser = authService.registerUser(signupRequest);
        return new ResponseEntity<>(newUser, HttpStatus.CREATED);
    }

    /**
     * Login user and get JWT token
     * POST /api/auth/login
     */
    @PostMapping("/login")
    public ResponseEntity<JwtResponse> loginUser(@RequestBody LoginRequest loginRequest) {
        JwtResponse jwtResponse = authService.loginUser(loginRequest);
        return new ResponseEntity<>(jwtResponse, HttpStatus.OK);
    }

    /**
     * Get current user info
     * GET /api/auth/user/{username}
     */
    @GetMapping("/user/{username}")
    public ResponseEntity<User> getUserInfo(@PathVariable String username) {
        User user = authService.getUserByUsername(username);
        return new ResponseEntity<>(user, HttpStatus.OK);
    }

    /**
     * Validate JWT token
     * POST /api/auth/validate
     */
    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
        String token = authHeader.substring(7);
        boolean isValid = authService.validateToken(token);
        return new ResponseEntity<>(isValid, HttpStatus.OK);
    }

    /**
     * Revoke a token
     * POST /api/auth/logout
     */
    @PostMapping("/logout")
    public ResponseEntity<String> revokeToken(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return new ResponseEntity<>("Invalid token format", HttpStatus.BAD_REQUEST);
        }
        String token = authHeader.substring(7);
        authService.revokeToken(token);
        return new ResponseEntity<>("Token revoked successfully", HttpStatus.OK);
    }

    /**
     * Get all active tokens for a user
     * GET /api/auth/user/{userId}/tokens
     */
    @GetMapping("/user/{userId}/tokens")
    public ResponseEntity<List<Token>> getUserTokens(@PathVariable Long userId) {
        List<Token> tokens = authService.getUserActiveTokens(userId);
        return new ResponseEntity<>(tokens, HttpStatus.OK);
    }

    /**
     * Health check endpoint
     */
    @GetMapping("/health")
    public ResponseEntity<String> health() {
        return new ResponseEntity<>("Auth API is running", HttpStatus.OK);
    }
}

