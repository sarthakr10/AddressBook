package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.AllArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users")
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String username;  // Used for login

    @Column(nullable = false, unique = true)
    private String email;  // Store actual email

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    private String role;  // ROLE_USER, ROLE_ADMIN
}
