package com.example.AddressBook.controller;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.service.AddressBookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Slf4j
@RestController
@RequestMapping("/api/addressbook") // Updated base path to match API calls
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    // Fetch all contacts
    @GetMapping
    public ResponseEntity<List<AddressBookDTO>> getAllContacts() {
        log.info("Fetching all contacts");
        List<AddressBookDTO> contacts = addressBookService.getAllContacts();
        return ResponseEntity.ok(contacts);
    }

    // Fetch a contact by ID
    @GetMapping("/{id}")
    public ResponseEntity<AddressBookDTO> getContactById(@PathVariable Long id) {
        log.info("Fetching contact with ID: {}", id);
        return addressBookService.getContactById(id)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Contact with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Add a new contact
    @PostMapping
    public ResponseEntity<AddressBookDTO> addContact(@RequestBody AddressBookDTO dto) {
        log.info("Adding new contact: {}", dto);
        AddressBookDTO savedContact = addressBookService.addContact(dto);
        return ResponseEntity.ok(savedContact);
    }

    // Update an existing contact
    @PutMapping("/{id}")
    public ResponseEntity<AddressBookDTO> updateContact(@PathVariable Long id, @RequestBody AddressBookDTO dto) {
        log.info("Updating contact with ID: {}", id);
        return addressBookService.updateContact(id, dto)
                .map(ResponseEntity::ok)
                .orElseGet(() -> {
                    log.warn("Unable to update, contact with ID {} not found", id);
                    return ResponseEntity.notFound().build();
                });
    }

    // Delete a contact by ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteContact(@PathVariable Long id) {
        log.info("Deleting contact with ID: {}", id);
        if (addressBookService.deleteContact(id)) {
            log.info("Contact with ID {} deleted successfully", id);
            return ResponseEntity.noContent().build();
        } else {
            log.warn("Contact with ID {} not found for deletion", id);
            return ResponseEntity.notFound().build();
        }
    }
}
