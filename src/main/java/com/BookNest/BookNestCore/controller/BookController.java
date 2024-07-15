package com.BookNest.BookNestCore.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class BookController {

    @GetMapping("/books")
    public String manageBooks(Model model) {
        // Ajoutez ici la logique pour récupérer et passer les données nécessaires à la vue
        return "books"; // Renvoie le template Thymeleaf books.html pour la gestion des livres
    }
}
