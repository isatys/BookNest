package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Contrôleur pour gérer les opérations d'authentification des utilisateurs.
 */
@Controller
public class LoginController {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserService userService;
    /**
     * Affiche le formulaire de connexion.
     *
     * @return le nom du template Thymeleaf pour la page de connexion.
     */
    @GetMapping("/login")
    public String loginPage() {
        return "login"; // Assurez-vous que "login" correspond au nom de votre template Thymeleaf ou JSP
    }

}
