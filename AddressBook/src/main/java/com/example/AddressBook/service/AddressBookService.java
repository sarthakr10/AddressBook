package com.example.AddressBook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.CachePut;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.cache.annotation.Caching;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private static final Logger logger = LoggerFactory.getLogger(AddressBookService.class);

    public AddressBookService(AddressBookRepository addressBookRepository) {
        this.addressBookRepository = addressBookRepository;
    }

    // ✅ Get all contacts with Caching
    @Cacheable(value = "contacts", key = "'allContacts'")
    public List<AddressBook> getAllContacts() {
        logger.info("Fetching contacts from Database (Not Cached)");
        return addressBookRepository.findAll();

    }

    // ✅ Get a specific contact by ID (Cached)
    @Cacheable(value = "contacts", key = "#id")
    public Optional<AddressBook> getContactById(Long id) {
        logger.info("Fetching contact {} from Database (Not Cached)", id);
        return addressBookRepository.findById(id);
    }

    // ✅ Add new contact (Evicts Cache)
    @CacheEvict(value = "contacts", allEntries = true)
    public AddressBook addContact(AddressBook contact) {
        logger.info("Adding new contact {} - Evicting Cache", contact.getName());
        return addressBookRepository.save(contact);
    }

    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "contacts", key = "'allContacts'"),  // Evict all contacts cache
            @CacheEvict(value = "contacts", key = "#id")  // Evict updated contact cache
    })
    @CachePut(value = "contacts", key = "#id")  // Update Redis with new contact data
    public AddressBook updateContact(Long id, AddressBook updatedContact) {
        logger.info("Updating contact {} - Updating Cache", id);
        return addressBookRepository.findById(id)
                .map(contact -> {
                    contact.setName(updatedContact.getName());
                    contact.setPhone(updatedContact.getPhone());
                    return addressBookRepository.save(contact);
                }).orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }



    @Caching(evict = {
            @CacheEvict(value = "contacts", key = "'allContacts'"),  // Evict all contacts cache
            @CacheEvict(value = "contacts", key = "#id")  // Remove deleted contact cache
    })
    public void deleteContact(Long id) {
        logger.info("Deleting contact {} - Evicting Cache", id);
        addressBookRepository.deleteById(id);
    }

}
