package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/livres")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/page")
    public String getAllLivres(Model model) {
        List<LivreDTO> livres = livreService.getAllLivres();
        model.addAttribute("page", livres);
        model.addAttribute("isAdmin", true); // Exemple : définir isAdmin à true ou false selon le contexte
        return "listLivres"; // Retourne le nom de la vue Thymeleaf
    }

    // Autres méthodes de contrôleur pour les vues HTML
}

