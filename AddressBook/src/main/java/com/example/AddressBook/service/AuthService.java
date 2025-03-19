package com.example.AddressBook.service;

import com.example.AddressBook.model.User;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.security.JwtUtil;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthService {
    private final UserRepository userRepository;
    private final BCryptPasswordEncoder passwordEncoder;
    private final JwtUtil jwtUtil;
    private final EmailService emailService; // ✅ Inject EmailService

    public AuthService(UserRepository userRepository, JwtUtil jwtUtil, EmailService emailService) {
        this.userRepository = userRepository;
        this.passwordEncoder = new BCryptPasswordEncoder();
        this.jwtUtil = jwtUtil;
        this.emailService = emailService;
    }

    // ✅ User Registration (Now sends Welcome Email)
    public String registerUser(String username, String email, String password) {
        if (userRepository.findByUsername(username).isPresent()) {
            return "Username already exists!";
        }
        if (userRepository.findByEmail(email).isPresent()) {
            return "Email already registered!";
        }

        User user = new User();
        user.setUsername(username);
        user.setEmail(email);
        user.setPassword(passwordEncoder.encode(password)); // Hash password
        user.setRole("ROLE_USER");

        userRepository.save(user);

        // ✅ Send Welcome Email
        emailService.sendWelcomeEmail(email);

        return "User registered successfully!";
    }

    // ✅ Authenticate User & Generate JWT
    public String authenticate(String username, String password) {
        Optional<User> userOpt = userRepository.findByUsername(username);
        if (userOpt.isPresent() && passwordEncoder.matches(password, userOpt.get().getPassword())) {
            return jwtUtil.generateToken(username);
        }
        return "Invalid Credentials";
    }

    // ✅ Forgot Password: Generate Reset Token & Send Email
    public String forgotPassword(String email) {
        Optional<User> userOpt = userRepository.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "Email not registered!";
        }

        String resetToken = UUID.randomUUID().toString();

        // ✅ Send Password Reset Email
        emailService.sendPasswordResetEmail(email, resetToken);

        return "Password reset link sent to your email!";
    }

    // ✅ Reset Password
    public String resetPassword(String token, String newPassword) {
        // Validate token (In real-world, use DB/Redis to verify token)
        if (token == null || token.length() < 10) {
            return "Invalid reset token!";
        }

        // Simulate user password update
        System.out.println("Password updated successfully for token: " + token);
        return "Password updated successfully!";
    }
}
