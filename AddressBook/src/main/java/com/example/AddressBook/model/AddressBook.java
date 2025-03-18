package com.example.AddressBook.model;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity  // ✅ JPA Entity for MySQL
@Data  // ✅ Lombok generates Getters, Setters, toString, equals, and hashCode
@NoArgsConstructor  // ✅ Generates a no-arg constructor
@AllArgsConstructor  // ✅ Generates an all-args constructor
public class AddressBook implements Serializable {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private String phone;
}
