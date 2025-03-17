package com.example.AddressBook.controller;

import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import java.util.Map;

// ✅ Import Logger for Logging
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    // ✅ Logger definition
    private static final Logger log = LoggerFactory.getLogger(AuthController.class);

    @Autowired
    private AuthService authService;

    // 🚀 Register Endpoint
    @PostMapping("/register")
    public ResponseEntity<String> registerUser(@RequestBody UserDTO userDTO) {
        log.info("Received registration request for: {}", userDTO.getUsername());

        String response = authService.registerUser(userDTO.getUsername(), userDTO.getEmail(), userDTO.getPassword());
        return ResponseEntity.ok(response);
    }

    // 🚀 Login Endpoint
    @PostMapping("/login")
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

    // 🚀 Forgot Password Endpoint
    @PostMapping("/forgot-password")
    public ResponseEntity<String> forgotPassword(@RequestBody Map<String, String> request) {
        String email = request.get("email");
        log.info("Received forgot password request for email: {}", email);

        String response = authService.forgotPassword(email);
        return ResponseEntity.ok(response);
    }

    // 🚀 Reset Password Endpoint
    @PostMapping("/reset-password")
    public ResponseEntity<String> resetPassword(@RequestBody Map<String, String> request) {
        String token = request.get("token");
        String newPassword = request.get("newPassword");
        log.info("Processing password reset request for token: {}", token);

        String response = authService.resetPassword(token, newPassword);
        return ResponseEntity.ok(response);
    }
}
