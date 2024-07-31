package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.service.LivreService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

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
        // Ajouter un nouvel objet LivreDTO pour le formulaire d'ajout
        model.addAttribute("newLivre", new LivreDTO());

        model.addAttribute("isAdmin", isAdmin);
        return "listLivres"; // Retourne le nom de la vue Thymeleaf
    }

    @PostMapping("/createLivre")
    public String createLivre(
            @Parameter(description = "Détails du livre à créer", required = true) @Valid @ModelAttribute LivreDTO livreDTO) {
        livreService.createLivre(livreDTO);
        return "redirect:/pages/livres"; // Rediriger vers la liste des livres après l'ajout
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        livreService.deleteLivre(id);
        return "redirect:/pages/livres";
    }


    @GetMapping("/editBook/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        LivreDTO livreDTO = livreService.getLivreById(id);
        model.addAttribute("livre", livreDTO);
        return "editBook"; // This should match the name of your Thymeleaf template (editBook.html)
    }

    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute LivreDTO livreDTO) {
        livreService.updateLivre(id, livreDTO);
        return "redirect:/pages/livres"; // Redirect to the book list page after updating
    }
}

