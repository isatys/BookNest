package com.booknest.booknestcore.service.impl;


import com.booknest.booknestcore.model.PasswordResetToken;
import com.booknest.booknestcore.model.User;
import com.booknest.booknestcore.repository.PasswordResetTokenRepository;
import com.booknest.booknestcore.repository.UserRepository;
import com.booknest.booknestcore.service.PasswordResetService;
import com.booknest.booknestcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;

@Service
public class PasswordResetServiceImpl implements PasswordResetService {

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private JavaMailSender mailSender;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;

    // Générer un jeton de réinitialisation
    public String generateResetToken() {
        return UUID.randomUUID().toString();
    }

    // Créer un jeton de réinitialisation pour un utilisateur
    public void createPasswordResetTokenForUser(String email) {
        // Vérifiez si l'utilisateur existe
        User user = userRepository.findByEmail(email);
        if (user == null) {
            // Optionnel : gérer le cas où l'utilisateur n'existe pas
            return;
        }

        // Créez un nouveau jeton
        String token = generateResetToken();
        PasswordResetToken resetToken = new PasswordResetToken();
        resetToken.setToken(token);
        resetToken.setEmail(email);
        resetToken.setExpiryDate(LocalDateTime.now().plusHours(1)); // 1 heure d'expiration

        // Enregistrez le jeton dans la base de données
        passwordResetTokenRepository.save(resetToken);

        // Envoyez un email avec le lien de réinitialisation
        String resetLink = "http://localhost:9091/reset-password?token=" + token;
        userService.sendEmail(email, "Réinitialisation de Mot de Passe", "Cliquez sur le lien suivant pour réinitialiser votre mot de passe : " + resetLink);
    }

    }
