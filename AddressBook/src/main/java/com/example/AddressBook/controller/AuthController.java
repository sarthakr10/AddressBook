package com.example.AddressBook.controller;

import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;
import io.swagger.v3.oas.annotations.tags.Tag;  // ✅ Import Swagger
import io.swagger.v3.oas.annotations.Operation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication Controller", description = "APIs for User Authentication")
public class AuthController {

    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    @PostMapping("/register")
    @Operation(summary = "Register a new user", description = "Creates a new user account.")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        log.info("Received registration request for: {}", userDTO.getUsername());
        String response = authService.registerUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok(response);
    }

    @PostMapping("/login")
    @Operation(summary = "Login a user", description = "Authenticate a user and return a JWT token.")
    public ResponseEntity<?> login(@RequestBody UserDTO userDTO) {
        log.info("Login attempt for user: {}", userDTO.getUsername());
        String token = authService.authenticate(userDTO.getUsername(), userDTO.getPassword());

        if (token.equals("Invalid Credentials")) {
            log.warn("Login failed for user: {}", userDTO.getUsername());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid username or password");
        }
        log.info("Login successful for user: {}", userDTO.getUsername());
        return ResponseEntity.ok(token);
    }

    @PostMapping("/forgot-password")
    @Operation(summary = "Forgot Password", description = "Sends a password reset link to the user's email.")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("Received forgot password request for email: {}", email);
        String response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    @PostMapping("/reset-password")
    @Operation(summary = "Reset Password", description = "Resets the user's password using a token.")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        log.info("Processing password reset request for token: {}", token);
        String response = authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(response);
    }
}
