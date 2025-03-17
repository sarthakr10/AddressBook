package com.example.AddressBook.service;

import com.example.AddressBook.dto.UserDTO;
import com.example.AddressBook.model.User;
import com.example.AddressBook.repository.UserRepository;
import com.example.AddressBook.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Service
public class AuthService {

    private static final Logger log = LoggerFactory.getLogger(AuthService.class);

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JwtUtil jwtUtil;

    private BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

    public String registerUser(UserDTO userDTO) {
        log.info("Attempting to register user: {}", userDTO.getUsername());

        if (userRepository.findByUsername(userDTO.getUsername()).isPresent()) {
            log.warn("Username {} is already taken", userDTO.getUsername());
            return "Username already taken";
        }

        User user = new User();
        user.setUsername(userDTO.getUsername());
        user.setPassword(passwordEncoder.encode(userDTO.getPassword()));
        user.setRole("ROLE_USER");
        userRepository.save(user);

        log.info("User {} registered successfully", userDTO.getUsername());
        return "User registered successfully";
    }

    public String authenticate(UserDTO userDTO) {
        log.info("Authenticating user: {}", userDTO.getUsername());

        Optional<User> userOptional = userRepository.findByUsername(userDTO.getUsername());
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            if (passwordEncoder.matches(userDTO.getPassword(), user.getPassword())) {
                String token = jwtUtil.generateToken(user.getUsername());
                log.info("JWT generated for user: {}", userDTO.getUsername());
                return token;
            } else {
                log.warn("Invalid password for user: {}", userDTO.getUsername());
            }
        } else {
            log.warn("User not found: {}", userDTO.getUsername());
        }
        return "Invalid Credentials";
    }
}
