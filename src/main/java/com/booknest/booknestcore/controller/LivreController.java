package com.booknest.booknestcore.controller;

import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.model.Auteur;
import com.booknest.booknestcore.repository.AuteurRepository;
import com.booknest.booknestcore.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
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

    @Operation(summary = "Récupérer tous les livres")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Livres récupérés avec succès"), @ApiResponse(responseCode = "404", description = "Aucun livre trouvé")})
    @GetMapping("/livres")
    public String getAllLivres(@RequestParam(value = "sort", required = false) String sort, @RequestParam(value = "genre", required = false) String genre, @RequestParam(value = "search", required = false) String search, @RequestParam(value = "page", defaultValue = "0") int page, @RequestParam(value = "size", defaultValue = "6") int size, Model model) {

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
                isAdmin = userDetails.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));
                // Ajout de log pour vérifier les autorités de l'utilisateur
                System.out.println("Authorities: " + userDetails.getAuthorities());
            }
        }
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("newLivre", new LivreDTO());

        return "listLivres"; // Retourne le nom de la vue Thymeleaf
    }


    @Operation(summary = "Créer un nouveau livre")
    @ApiResponses(value = {@ApiResponse(responseCode = "201", description = "Livre créé avec succès"), @ApiResponse(responseCode = "400", description = "Requête invalide")})
    @PostMapping("/createLivre")
    public String createLivre(@Parameter(description = "Détails du livre à créer", required = true) @Valid @ModelAttribute LivreDTO livreDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.createLivre(livreDTO);
        return "redirect:/pages/livres"; // Rediriger vers la liste des livres après l'ajout
    }

    @GetMapping("/createLivre")
    public String showCreateForm(Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }

        // Créer un nouvel objet LivreDTO
        LivreDTO newLivre = new LivreDTO();
        model.addAttribute("newLivre", newLivre);

        // Charger la liste des auteurs et genres
        List<Auteur> auteurs = auteurRepository.findAll();
        model.addAttribute("auteurs", auteurs);

        List<String> genres = livreService.getDistinctGenres();
        model.addAttribute("genres", genres);

        return "createLivre"; // Nom de la vue Thymeleaf pour le formulaire de création
    }

    @Operation(summary = "Supprimer un livre par ID")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Livre supprimé avec succès"), @ApiResponse(responseCode = "404", description = "Livre non trouvé")})
    @GetMapping("/deleteBook/{id}")
    public String deleteBook(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.deleteLivre(id);
        return "redirect:/pages/livres";
    }

    @GetMapping("/editBook/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        LivreDTO livreDTO = livreService.getLivreById(id);
        List<Auteur> auteurs = livreService.getAllAuteurs(); // Assurez-vous que la méthode getAllAuteurs() existe

        model.addAttribute("livre", livreDTO);
        model.addAttribute("auteurs", auteurs); // Ajout de la liste des auteurs au modèle
        return "editBook"; // This should match the name of your Thymeleaf template (editBook.html)
    }

    @Operation(summary = "Met à jour un livre existant")
    @ApiResponses(value = {@ApiResponse(responseCode = "200", description = "Livre mis à jour avec succès"), @ApiResponse(responseCode = "400", description = "Requête invalide"), @ApiResponse(responseCode = "404", description = "Livre non trouvé"), @ApiResponse(responseCode = "403", description = "Accès refusé pour les utilisateurs non-admin")})
    @PostMapping("/updateBook/{id}")
    public String updateBook(@PathVariable Long id, @ModelAttribute LivreDTO livreDTO) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.getAuthorities().stream().anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"))) {
            return "redirect:/pages/livres"; // Redirige vers la liste des livres si non-admin
        }
        livreService.updateLivre(id, livreDTO);
        return "redirect:/pages/livres"; // Redirect to the book list page after updating
    }
}
