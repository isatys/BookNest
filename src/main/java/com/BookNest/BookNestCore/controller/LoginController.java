package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour gérer les opérations d'authentification et d'inscription des utilisateurs.
 */
@Controller
public class LoginController {

    @Autowired
    private UserService userService;

    @Autowired
    private PasswordEncoder passwordEncoder;
    /**
     * Affiche le formulaire de connexion.
     *
     * @return le nom du template Thymeleaf pour la page de connexion.
     */
    @GetMapping("/login")
    public String showLoginForm() {
        return "login"; // Renvoie le template Thymeleaf login.html
    }

    /**
     * Gère la soumission du formulaire de connexion.
     *
     * @param username le nom d'utilisateur.
     * @param password le mot de passe.
     * @param model le modèle pour passer des données à la vue.
     * @return une redirection vers la page d'accueil après une connexion réussie.
     */
    @PostMapping("/login")
    public String loginSubmit(@RequestParam String username, @RequestParam String password, Model model) {
        User user = userService.findByUsername(username);

        if (user != null && passwordEncoder.matches(password, user.getPassword())) {
            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            if (authentication.isAuthenticated()) {
                model.addAttribute("user", user);
                return "redirect:/"; // Redirige vers la page d'accueil après la connexion réussie
            }
        }
        model.addAttribute("error", "Nom d'utilisateur ou mot de passe incorrect");
        return "login"; // Redirige vers la page de connexion en cas d'échec
    }

}
