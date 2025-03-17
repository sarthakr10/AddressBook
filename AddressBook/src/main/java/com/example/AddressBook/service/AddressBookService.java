package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {
    private final List<AddressBook> addressBookList = new ArrayList<>();
    private Long idCounter = 1L; // Change int to Long

    // Fetch all contacts
    public List<AddressBookDTO> getAllContacts() {
        List<AddressBookDTO> dtos = new ArrayList<>();
        for (AddressBook contact : addressBookList) {
            dtos.add(convertModelToDto(contact));
        }
        return dtos;
    }

    // Fetch a contact by ID
    public Optional<AddressBookDTO> getContactById(Long id) { // Change int to Long
        return addressBookList.stream()
                .filter(contact -> contact.getId().equals(id)) // Use equals() for Long comparison
                .findFirst()
                .map(this::convertModelToDto);
    }

    // Add a new contact
    public AddressBookDTO addContact(AddressBookDTO dto) {
        AddressBook contact = convertDtoToModel(dto);
        contact.setId(idCounter++); // Assign unique ID (Long)
        addressBookList.add(contact);
        return convertModelToDto(contact);
    }

    // Update contact by ID
    public Optional<AddressBookDTO> updateContact(Long id, AddressBookDTO dto) { // Change int to Long
        for (AddressBook contact : addressBookList) {
            if (contact.getId().equals(id)) { // Use equals() for Long comparison
                contact.setName(dto.getName());
                contact.setEmail(dto.getEmail());
                contact.setPhoneNumber(dto.getPhoneNumber());
                return Optional.of(convertModelToDto(contact));
            }
        }
        return Optional.empty();
    }

    // Delete contact by ID
    public boolean deleteContact(Long id) { // Change int to Long
        return addressBookList.removeIf(contact -> contact.getId().equals(id)); // Use equals() for Long comparison
    }

    // Helper method to convert DTO to Model
    private AddressBook convertDtoToModel(AddressBookDTO dto) {
        AddressBook contact = new AddressBook();
        contact.setName(dto.getName());
        contact.setEmail(dto.getEmail());
        contact.setPhoneNumber(dto.getPhoneNumber());
        return contact;
    }

    // Helper method to convert Model to DTO
    private AddressBookDTO convertModelToDto(AddressBook contact) {
        return new AddressBookDTO(contact.getName(), contact.getEmail(), contact.getPhoneNumber());
    }
}
