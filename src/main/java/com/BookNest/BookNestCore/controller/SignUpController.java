package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    /**
     * Affiche le formulaire d'inscription.
     *
     * @param model le modèle pour passer des données à la vue.
     * @return le nom du template Thymeleaf pour la page d'inscription.
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Gère la soumission du formulaire d'inscription.
     *
     * @param username l'utilisateur à enregistrer.
     * @return une redirection vers la page de connexion après une inscription réussie.
     */
    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password, @RequestParam String address, Model model) {
        // Vérifier si l'utilisateur existe déjà
        User existingUser = userService.findByUsername(username);
       if (existingUser != null) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "signup";
        }


        User utilisateur = new User();
        utilisateur.setUsername(username);
        utilisateur.setPassword(password);
        userService.saveUser(utilisateur);
        return "redirect:/login";
    }
}
