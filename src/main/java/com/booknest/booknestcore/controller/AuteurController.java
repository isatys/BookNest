package com.booknest.booknestcore.controller;

import com.booknest.booknestcore.dto.AuteurDTO;
import com.booknest.booknestcore.mapper.AuteurMapper;
import com.booknest.booknestcore.repository.AuteurRepository;
import com.booknest.booknestcore.service.AuteurService;
import com.booknest.booknestcore.service.LivreService;
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
public class AuteurController {

    @Autowired
    private AuteurService auteurService;
    @Autowired
    private AuteurRepository auteurRepository;
    @Autowired
    private AuteurMapper auteurMapper;
    @Autowired
    private LivreService livreService;

    @GetMapping("/auteurs")
    public String getAllAuteurs( @RequestParam(value = "search", required = false) String search,
                                 @RequestParam(value = "authorFilter", required = false) String authorFilter,Model model) {
        List<AuteurDTO> auteurs = auteurService.getAllAuthors();

        // Si une recherche est effectuée, rediriger vers la page des livres avec le paramètre de recherche
        if (search != null && !search.isEmpty()) {
            return "redirect:/pages/livres?search=" + search;
        }

        if (authorFilter != null && !authorFilter.isEmpty()) {
            // Appliquer le filtre de nom d'auteur
            auteurs = auteurService.searchAuthorsByName(authorFilter);
        }

        model.addAttribute("auteurs", auteurs);

        boolean isAdmin = checkIfUserIsAdmin();



        model.addAttribute("newAuteur", new AuteurDTO());
        model.addAttribute("isAdmin", isAdmin);

        return "listAuteurs"; // Return the Thymeleaf template name
    }

    @PostMapping("/createAuthor")
    public String createAuteur(
            @Parameter(description = "Détails de l'auteur à créer", required = true) @Valid @ModelAttribute AuteurDTO auteurDTO) {
        if (!checkIfUserIsAdmin()) {
            return "redirect:/pages/auteurs"; // Redirect if the user is not an admin
        }
        auteurService.createAuthor(auteurDTO);
        return "redirect:/pages/auteurs";
    }

    @GetMapping("/createAuthor")
    public String showCreateForm(Model model) {
        if (!checkIfUserIsAdmin()) {
            return "redirect:/pages/auteurs"; // Redirige vers la liste des auteurs si non-admin
        }

        model.addAttribute("newAuteur", new AuteurDTO());
        return "createAuteur"; // Nom de la vue Thymeleaf pour le formulaire de création
    }


    @GetMapping("/deleteAuthor/{id}")
    public String deleteAuteur(@PathVariable Long id) {
        if (!checkIfUserIsAdmin()) {
            return "redirect:/pages/auteurs"; // Redirect if the user is not an admin
        }
        auteurService.deleteAuthor(id);
        return "redirect:/pages/auteurs";
    }

    @GetMapping("/editAuteur/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        if (!checkIfUserIsAdmin()) {
            return "redirect:/pages/auteurs"; // Redirect if the user is not an admin
        }
        AuteurDTO auteurDTO = auteurService.getAuthorById(id);
        model.addAttribute("auteur", auteurDTO);
        return "editAuteur"; // This should match the name of your Thymeleaf template (editAuteur.html)
    }

    @PostMapping("/updateAuteur/{id}")
    public String updateAuteur(@PathVariable Long id, @ModelAttribute AuteurDTO auteurDTO) {
        if (!checkIfUserIsAdmin()) {

            return "redirect:/pages/auteurs"; // Redirect if the user is not an admin
        }
        auteurService.updateAuteur(id, auteurDTO);
        return "redirect:/pages/auteurs";
    }

    private boolean checkIfUserIsAdmin() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                return userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            }
        }
        return false;
    }
}
