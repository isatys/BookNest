package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.model.PasswordResetToken;
import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.repository.PasswordResetTokenRepository;
import com.BookNest.BookNestCore.repository.UserRepository;
import com.BookNest.BookNestCore.service.PasswordResetService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.time.LocalDateTime;

@Controller
public class PasswordResetController {

    @Autowired
    private PasswordResetService passwordResetService;

    @Autowired
    private PasswordResetTokenRepository passwordResetTokenRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping("/forgot-password")
    public String showForgotPasswordForm() {
        return "forgot-password";
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, RedirectAttributes redirectAttributes) {
        passwordResetService.createPasswordResetTokenForUser(email);
        redirectAttributes.addFlashAttribute("message", "Instructions de réinitialisation envoyées à votre adresse email.");
        return "redirect:/forgot-password";
    }

    @GetMapping("/reset-password")
    public String showResetPasswordForm(@RequestParam String token, Model model) {
        model.addAttribute("token", token);
        return "reset-password";
    }

    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String password, RedirectAttributes redirectAttributes) {
        // Validez le jeton et mettez à jour le mot de passe
        PasswordResetToken resetToken = passwordResetTokenRepository.findByToken(token);
        if (resetToken == null || resetToken.getExpiryDate().isBefore(LocalDateTime.now())) {
            redirectAttributes.addFlashAttribute("error", "Jeton invalide ou expiré.");
            return "redirect:/reset-password?token=" + token;
        }

        User user = userRepository.findByEmail(resetToken.getEmail());
        if (user != null) {
            user.setPassword(passwordEncoder.encode(password)); // Assurez-vous d'utiliser un encodeur de mot de passe
            userRepository.save(user);
        }

        // Supprimez le jeton après utilisation
        passwordResetTokenRepository.delete(resetToken);

        redirectAttributes.addFlashAttribute("message", "Votre mot de passe a été réinitialisé avec succès.");
        return "redirect:/login";
    }
}