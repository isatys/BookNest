package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/pages")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping("/livres")
    public String getAllLivres(Model model) {
        List<LivreDTO> livres = livreService.getAllLivres();
        model.addAttribute("livres", livres);
        // Vérifier si l'utilisateur a le rôle d'administrateur
        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
                // Debug
                System.out.println("isAdmin: " + isAdmin);
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        return "listLivres"; // Retourne le nom de la vue Thymeleaf
    }

    // Autres méthodes de contrôleur pour les vues HTML
}

