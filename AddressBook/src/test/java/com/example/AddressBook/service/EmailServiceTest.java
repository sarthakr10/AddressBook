package com.example.AddressBook.service;

import static org.mockito.Mockito.*;

import jakarta.mail.internet.MimeMessage;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.javamail.JavaMailSender;

class EmailServiceTest {

    @Mock
    private JavaMailSender javaMailSender; // ✅ Mock JavaMailSender

    @InjectMocks
    private EmailService emailService; // ✅ Inject mocks

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSendWelcomeEmail_Success() {
        // ✅ Arrange
        MimeMessage mockMessage = mock(MimeMessage.class);
        when(javaMailSender.createMimeMessage()).thenReturn(mockMessage);

        // ✅ Act
        emailService.sendWelcomeEmail("sarthak@example.com");

        // ✅ Assert
        verify(javaMailSender, times(1)).send(mockMessage); // ✅ Ensure email is sent
    }
}
