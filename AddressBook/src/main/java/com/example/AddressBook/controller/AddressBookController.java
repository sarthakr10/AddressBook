package com.example.AddressBook.controller;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.service.AddressBookService;
import org.springframework.web.bind.annotation.*;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/contacts")
public class AddressBookController {

    private final AddressBookService addressBookService;

    public AddressBookController(AddressBookService addressBookService) {
        this.addressBookService = addressBookService;
    }

    // ✅ Get all contacts (Cached)
    @GetMapping
    public List<AddressBook> getAllContacts() {
        return addressBookService.getAllContacts();
    }

    // ✅ Get a single contact by ID (Cached)
    @GetMapping("/{id}")
    public Optional<AddressBook> getContactById(@PathVariable Long id) {
        return addressBookService.getContactById(id);
    }

    // ✅ Add a new contact (Cache Evicted)
    @PostMapping
    public AddressBook addContact(@RequestBody AddressBook contact) {
        return addressBookService.addContact(contact);
    }

    // ✅ Update a contact (Cache Evicted)
    @PutMapping("/{id}")
    public AddressBook updateContact(@PathVariable Long id, @RequestBody AddressBook contact) {
        return addressBookService.updateContact(id, contact);
    }

    // ✅ Delete a contact (Cache Evicted)
    @DeleteMapping("/{id}")
    public void deleteContact(@PathVariable Long id) {
        addressBookService.deleteContact(id);
    }
}
