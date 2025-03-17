package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String username;  // Keep this for login
    private String email;     // Store actual email
    private String password;
    private String role;      // ROLE_USER, ROLE_ADMIN
}
