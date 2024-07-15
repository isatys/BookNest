package com.BookNest.BookNestCore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class AccueilController {

    @GetMapping("/accueil")
    public String manageBooks(Model model) {
        // Ajoutez ici la logique pour récupérer et passer les données nécessaires à la vue
        return "accueil"; // Renvoie le template Thymeleaf accueil.html pour la gestion des livres
    }
}
