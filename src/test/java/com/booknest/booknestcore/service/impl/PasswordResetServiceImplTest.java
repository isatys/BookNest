package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.model.PasswordResetToken;
import com.booknest.booknestcore.model.User;
import com.booknest.booknestcore.repository.PasswordResetTokenRepository;
import com.booknest.booknestcore.repository.UserRepository;
import com.booknest.booknestcore.service.UserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


 class PasswordResetServiceImplTest {

    @Mock
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UserService userService;

    @InjectMocks
    private PasswordResetServiceImpl passwordResetService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    // Test pour la méthode generateResetToken
    @Test
     void generateResetToken_ShouldReturnValidUUID() {
        // Act
        String token = passwordResetService.generateResetToken();

        // Assert
        assertNotNull(token);
        assertDoesNotThrow(() -> UUID.fromString(token)); // Vérifie que le token est un UUID valide
    }

    // Test pour la méthode createPasswordResetTokenForUser quand l'utilisateur existe
    @Test
     void createPasswordResetTokenForUser_ShouldCreateAndSendEmail_WhenUserExists() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        passwordResetService.createPasswordResetTokenForUser(email);

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordResetTokenRepository, times(1)).save(any(PasswordResetToken.class));
        verify(userService, times(1)).sendEmail(eq(email), anyString(), anyString());
    }

    // Test pour la méthode createPasswordResetTokenForUser quand l'utilisateur n'existe pas
    @Test
     void createPasswordResetTokenForUser_ShouldDoNothing_WhenUserDoesNotExist() {
        // Arrange
        String email = "nonexistent@example.com";

        when(userRepository.findByEmail(email)).thenReturn(null);

        // Act
        passwordResetService.createPasswordResetTokenForUser(email);

        // Assert
        verify(userRepository, times(1)).findByEmail(email);
        verify(passwordResetTokenRepository, times(0)).save(any(PasswordResetToken.class));
        verify(userService, times(0)).sendEmail(anyString(), anyString(), anyString());
    }

    // Test pour vérifier si le jeton est bien sauvegardé
    @Test
     void createPasswordResetTokenForUser_ShouldSaveResetTokenWithCorrectValues() {
        // Arrange
        String email = "user@example.com";
        User user = new User();
        user.setEmail(email);

        when(userRepository.findByEmail(email)).thenReturn(user);

        // Act
        passwordResetService.createPasswordResetTokenForUser(email);

        // Assert
        verify(passwordResetTokenRepository, times(1)).save(argThat(resetToken ->
                resetToken.getEmail().equals(email) &&
                        resetToken.getToken() != null &&
                        resetToken.getExpiryDate().isAfter(LocalDateTime.now())
        ));
    }
}
