package com.example.AddressBook.service;

import com.example.AddressBook.dto.UserDTO; // Import UserDTO
import com.example.AddressBook.model.PasswordResetToken;
import com.example.AddressBook.model.User;
import com.example.AddressBook.repository.PasswordResetTokenRepository;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;

    @Autowired
    private EmailService emailService;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(String username, String email, String password) {
        log.info("Attempting to register user: {}", username);

        if (userRepository.findByUsername(username).isPresent()) {
            log.warn("Username {} is already taken", username);
            return "Username already taken";
        }

        if (userRepository.findByEmail(email).isPresent()) {
            log.warn("Email {} is already registered", email);
            return "Email already registered";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        log.info("User {} registered successfully", username);
        return "User registered successfully";
    }


    public String authenticate(String username, String password) {
        log.info("Authenticating user: {}", username);

        Optional<User> userOptional = userRepository.findByUsername(username);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(password, user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                log.info("JWT generated for user: {}", username);
                return token;
            } else {
                log.warn("Invalid password for user: {}", username);
            }
        } else {
            log.warn("User not found: {}", username);
        }
        return "Invalid Credentials";
    }

    public String forgotPassword(String email) {
        log.info("Processing forgot password request for email: {}", email);

        Optional<User> userOpt = userRepository.findByEmail(email);  // FIXED: Now using email
        if (userOpt.isEmpty()) {
            log.warn("User with email {} not found", email);
            return "User not found";
        }

        // Generate reset token
        String resetToken = UUID.randomUUID().toString();
        LocalDateTime expiryTime = LocalDateTime.now().plusMinutes(30); // Token expires in 30 minutes

        // Save token
        PasswordResetToken token = new PasswordResetToken();
        token.setToken(resetToken);
        token.setEmail(email);
        token.setExpiryDate(expiryTime);
        tokenRepository.save(token);

        // Send reset email
        String resetLink = "http://localhost:8080/api/auth/reset-password?token=" + resetToken;
        emailService.sendEmail(email, "Password Reset Request",
                "Click the link to reset your password: " + resetLink);

        log.info("Password reset email sent to {}", email);
        return "Password reset link sent!";
    }


    // 🚀 Reset Password Implementation 🚀
    public String resetPassword(String token, String newPassword) {
        log.info("Processing password reset with token: {}", token);

        Optional<PasswordResetToken> tokenOpt = tokenRepository.findByToken(token);
        if (tokenOpt.isEmpty()) {
            log.warn("Invalid reset token: {}", token);
            return "Invalid reset token";
        }

        PasswordResetToken resetToken = tokenOpt.get();
        if (resetToken.isExpired()) {
            log.warn("Expired reset token: {}", token);
            return "Reset token expired";
        }

        // Find user by email
        Optional<User> userOpt = userRepository.findByUsername(resetToken.getEmail());
        if (userOpt.isEmpty()) {
            log.warn("User not found for reset token: {}", token);
            return "User not found";
        }

        // Update password
        User user = userOpt.get();
        user.setPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        // Delete the used reset token
        tokenRepository.delete(resetToken);

        log.info("Password reset successful for user: {}", user.getUsername());
        return "Password reset successful!";
    }
}
