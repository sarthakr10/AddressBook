package com.example.AddressBook.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.cache.annotation.*;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import com.example.AddressBook.model.AddressBook;
import com.example.AddressBook.repository.AddressBookRepository;
import java.util.List;
import java.util.Optional;

@Service
public class AddressBookService {

    private final AddressBookRepository addressBookRepository;
    private final RabbitTemplate rabbitTemplate;
    private static final Logger logger = LoggerFactory.getLogger(AddressBookService.class);
    private static final String EXCHANGE_NAME = "addressbook.exchange";
    private static final String ROUTING_KEY = "contact.events";

    public AddressBookService(AddressBookRepository addressBookRepository, RabbitTemplate rabbitTemplate) {
        this.addressBookRepository = addressBookRepository;
        this.rabbitTemplate = rabbitTemplate;
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

    // ✅ Add new contact (Evicts Cache and Publishes Event)
    @CacheEvict(value = "contacts", allEntries = true)
    public AddressBook addContact(AddressBook contact) {
        logger.info("Adding new contact {} - Evicting Cache", contact.getName());
        AddressBook savedContact = addressBookRepository.save(contact);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "Contact Added: " + savedContact.getName());
        logger.info("Published Contact Added event to RabbitMQ");

        return savedContact;
    }

    // ✅ Update contact (Evicts Cache, Updates DB, and Publishes Event)
    @Transactional
    @Caching(evict = {
            @CacheEvict(value = "contacts", key = "'allContacts'"),
            @CacheEvict(value = "contacts", key = "#id")
    })
    @CachePut(value = "contacts", key = "#id")
    public AddressBook updateContact(Long id, AddressBook updatedContact) {
        logger.info("Updating contact {} - Updating Cache", id);
        return addressBookRepository.findById(id)
                .map(contact -> {
                    contact.setName(updatedContact.getName());
                    contact.setPhone(updatedContact.getPhone());
                    AddressBook savedContact = addressBookRepository.save(contact);

                    // Publish event to RabbitMQ
                    rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "Contact Updated: " + savedContact.getName());
                    logger.info("Published Contact Updated event to RabbitMQ");

                    return savedContact;
                }).orElseThrow(() -> new RuntimeException("Contact not found with id: " + id));
    }

    // ✅ Delete contact (Evicts Cache and Publishes Event)
    @Caching(evict = {
            @CacheEvict(value = "contacts", key = "'allContacts'"),
            @CacheEvict(value = "contacts", key = "#id")
    })
    public void deleteContact(Long id) {
        logger.info("Deleting contact {} - Evicting Cache", id);
        addressBookRepository.deleteById(id);

        // Publish event to RabbitMQ
        rabbitTemplate.convertAndSend(EXCHANGE_NAME, ROUTING_KEY, "Contact Deleted: " + id);
        logger.info("Published Contact Deleted event to RabbitMQ");
    }
}
