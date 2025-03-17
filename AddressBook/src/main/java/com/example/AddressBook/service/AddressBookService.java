package com.example.AddressBook.service;

import com.example.AddressBook.dto.AddressBookDTO;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class AddressBookService {
    @Autowired
    private AddressBookRepository repository;

    // Fetch all contacts
    public List<AddressBookDTO> getAllContacts() {
        List<AddressBook> contacts = repository.findAll();
        return contacts.stream()
                .map(contact -> new AddressBookDTO(contact.getName(), contact.getEmail(), contact.getPhoneNumber()))
                .collect(Collectors.toList());
    }

    // Add a new contact
    public AddressBookDTO addContact(AddressBookDTO dto) {
        AddressBook contact = convertDtoToModel(dto);
        AddressBook savedContact = repository.save(contact);
        return convertModelToDto(savedContact);
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
