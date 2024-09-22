package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.model.Role;
import com.booknest.booknestcore.model.User;
import com.booknest.booknestcore.repository.EmpruntRepository;
import com.booknest.booknestcore.repository.RoleRepository;
import com.booknest.booknestcore.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private JavaMailSender mailSender;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void saveUser_ShouldSaveUser() {
        // Arrange
        User user = new User();
        user.setUsername("testuser");

        when(userRepository.save(user)).thenReturn(user);

        // Act
        User result = userService.saveUser(user);

        // Assert
        assertNotNull(result);
        assertEquals("testuser", result.getUsername());
        verify(userRepository).save(user);
    }

    @Test
    void findByUsername_ShouldReturnUser() {
        // Arrange
        String username = "testuser";
        User user = new User();
        user.setUsername(username);

        when(userRepository.findByUsername(username)).thenReturn(user);

        // Act
        User result = userService.findByUsername(username);

        // Assert
        assertNotNull(result);
        assertEquals(username, result.getUsername());
        verify(userRepository).findByUsername(username);
    }

    @Test
    void addAdminUser_ShouldAddAdminRole() {
        // Arrange
        String username = "admin";
        String password = "password";

        // Création d'un utilisateur pour la vérification
        User user = new User();
        user.setUsername(username);

        Role adminRole = new Role();
        adminRole.setName("ROLE_ADMIN");

        // Configuration des mocks
        when(roleRepository.findByName("ROLE_ADMIN")).thenReturn(Optional.of(adminRole));
        when(passwordEncoder.encode(password)).thenReturn("encodedPassword");

        // La méthode save() doit être appelée avec un utilisateur contenant le rôle admin
        doAnswer(invocation -> {
            User savedUser = invocation.getArgument(0);
            savedUser.getRoles().add(adminRole);
            return savedUser;
        }).when(userRepository).save(any(User.class));

        // Act
        userService.addAdminUser(username, password);

        // Assert
        // Vérifiez que la méthode save() a été appelée avec l'utilisateur correctement configuré
        verify(userRepository).save(argThat(userArg ->
                userArg.getRoles().contains(adminRole) &&
                        userArg.getUsername().equals(username) &&
                        userArg.getPassword().equals("encodedPassword")));
    }


    @Test
    void sendEmail_ShouldSendEmail() {
        // Arrange
        String to = "recipient@example.com";
        String subject = "Subject";
        String text = "Email body";

        // Act
        userService.sendEmail(to, subject, text);

        // Assert
        SimpleMailMessage expectedMessage = new SimpleMailMessage();
        expectedMessage.setFrom("isatys.riviere@free.fr");
        expectedMessage.setTo(to);
        expectedMessage.setSubject(subject);
        expectedMessage.setText(text);

        verify(mailSender).send(expectedMessage);
    }
}
