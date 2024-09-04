package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.repository.AuteurRepository;
import com.BookNest.BookNestCore.service.LivreService;
import io.swagger.v3.oas.annotations.Parameter;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @Autowired
    private AuteurRepository auteurRepository;

    @GetMapping("/livres")
    public String getAllLivres(
            @RequestParam(value = "sort", required = false) String sort,
            @RequestParam(value = "genre", required = false) String genre,
            @RequestParam(value = "search", required = false) String search,
            @RequestParam(value = "page", defaultValue = "0") int page,
            @RequestParam(value = "size", defaultValue = "6") int size,
            Model model) {

        List<LivreDTO> livres;

        livres = livreService.getAllLivres();

        Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "titre"));
        Page<LivreDTO> livresPage = livreService.getAllLivresPage(pageable);

        // Filtrer les livres en fonction du genre et de la recherche
        if (genre != null && !genre.isEmpty()) {
            livresPage = livreService.getLivresByGenre(genre, pageable); // Assurez-vous que ce service supporte la pagination
        } else if (search != null && !search.isEmpty()) {
            livresPage = livreService.searchLivres(search, pageable); // Assurez-vous que ce service supporte la pagination
        }

        model.addAttribute("livresPage", livresPage);

        model.addAttribute("livres", livres);

        // Charger les auteurs et autres attributs comme avant
        List<Auteur> auteurs = auteurRepository.findAll();
        model.addAttribute("auteurs", auteurs);

        // Récupérer la liste des genres distincts pour le filtre
        List<String> genres = livreService.getDistinctGenres();
        model.addAttribute("genres", genres);

        // Gérer l'affichage pour l'admin
        boolean isAdmin = false;
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth != null) {
            Object principal = auth.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                isAdmin = userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("newLivre", new LivreDTO());

        return "listLivres"; // Retourne le nom de la vue Thymeleaf
    }



    @PostMapping("/createLivre")
    public String createLivre(
            @Parameter(description = "Détails du livre à créer", required = true) @Valid @ModelAttribute LivreDTO livreDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.createLivre(livreDTO);
        return "redirect:/pages/livres"; // Rediriger vers la liste des livres après l'ajout
    }

    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.deleteLivre(id);
        return "redirect:/pages/livres";
    }

    @GetMapping("/editBook/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        LivreDTO livreDTO = livreService.getLivreById(id);
        List<Auteur> auteurs = livreService.getAllAuteurs(); // Assurez-vous que la méthode getAllAuteurs() existe

        model.addAttribute("livre", livreDTO);
        model.addAttribute("auteurs", auteurs); // Ajout de la liste des auteurs au modèle
        return "editBook"; // This should match the name of your Thymeleaf template (editBook.html)
    }

    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute LivreDTO livreDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.updateLivre(id, livreDTO);
        return "redirect:/pages/livres"; // Redirect to the book list page after updating
    }
}
