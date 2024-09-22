package com.booknest.booknestcore.controller;

import com.booknest.booknestcore.model.User;
import com.booknest.booknestcore.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.security.Principal;

@Controller
@RequestMapping("/pages")
public class UserProfileController {

    @Autowired
    private UserRepository userRepository;  // Assurez-vous d'avoir un repository pour l'entité User

    @Autowired
    private PasswordEncoder passwordEncoder; // Injecter le PasswordEncoder


    @GetMapping("/profile")
    public String showProfile(Model model, Principal principal) {
        // Récupérer l'utilisateur connecté
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();
        User user = userRepository.findByUsername(currentUsername);
        model.addAttribute("user", user);
        return "profile";
    }

    @PostMapping("/updateProfile")
    public String updateProfile(@RequestParam("password") String password, RedirectAttributes redirectAttributes) {
        // Récupérer l'utilisateur connecté à partir du contexte de sécurité
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        String currentUsername = authentication.getName();

        // Charger l'utilisateur actuel depuis la base de données
        User existingUser = userRepository.findByUsername(currentUsername);

        if (existingUser == null) {
            throw new RuntimeException("User not found");
        }

        // Mettre à jour uniquement le mot de passe si le nouveau mot de passe n'est pas vide
        if (password != null && !password.isEmpty()) {
            existingUser.setPassword(passwordEncoder.encode(password)); // Encoder le mot de passe
            userRepository.save(existingUser);

            // Ajouter un message de succès au modèle
            redirectAttributes.addFlashAttribute("message", "Mot de passe mis à jour avec succès !");
        } else {
            // Ajouter un message d'erreur si le mot de passe est vide
            redirectAttributes.addFlashAttribute("message", "Le mot de passe ne peut pas être vide.");
        }


        return "redirect:/pages/profile";// Retourner la vue du profil pour afficher le message
    }



}

