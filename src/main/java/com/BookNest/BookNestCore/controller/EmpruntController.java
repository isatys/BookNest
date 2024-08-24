package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.EmpruntService;
import com.BookNest.BookNestCore.service.LivreService;
import com.BookNest.BookNestCore.service.UserService;
import com.BookNest.BookNestCore.service.UtilisateurService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/pages")
public class EmpruntController {

    @Autowired
    private EmpruntService empruntService;

    @Autowired
    private UserService userService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private LivreService livreService;

    @GetMapping("/emprunts")
    public String getAllEmprunts(Model model) {
        // Récupérer le nom de l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Récupérer l'utilisateur de la table User
        User user = userService.findByUsername(username);

        if (user == null) {
            // Gestion d'erreur si l'utilisateur n'existe pas
            model.addAttribute("errorMessage", "Utilisateur non trouvé.");
            return "error"; // Assurez-vous d'avoir une vue d'erreur
        }
        // Récupérer Utilisateur à partir de User
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(user.getUsername());
        Long utilisateurId = utilisateur.getId();
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        List<EmpruntDTO> emprunts;

        if (isAdmin) {
            // Si l'utilisateur est admin, obtenir tous les emprunts
            emprunts = empruntService.getAllEmpruntsForAdmin();
        } else {
            // Sinon, obtenir les emprunts de l'utilisateur spécifique
            emprunts = empruntService.getEmpruntsByUtilisateur(utilisateurId);
        }

        if (emprunts.isEmpty()) {
            model.addAttribute("infoMessage", "Aucun emprunt trouvé.");
        } else {
            model.addAttribute("emprunts", emprunts);
        }

        model.addAttribute("isAdmin", isAdmin);

        // Ajouter un nouvel emprunt par défaut pour éviter l'exception
        model.addAttribute("emprunt", new EmpruntDTO());

        // Ajouter les livres disponibles au modèle
        List<LivreDTO> livres = livreService.getAllLivres();
        model.addAttribute("livres", livres);

        // Ajouter les utilisateurs disponibles au modèle (si nécessaire)
        if (isAdmin) {
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("utilisateurs", utilisateurs);
        }

        // Retourner la vue appropriée
        return isAdmin ? "adminEmprunts" : "userEmprunts";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/editEmprunt/{id}")
    public String editEmprunt(@PathVariable Long id, Model model) {
        EmpruntDTO emprunt = empruntService.getEmpruntById(id);
        List<LivreDTO> livres = livreService.getAllLivres();
        List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();

        model.addAttribute("emprunt", emprunt);
        model.addAttribute("livres", livres);
        model.addAttribute("utilisateurs", utilisateurs);

        return "editEmprunt";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/createEmprunt")
    public String createEmprunt(@ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être après la date de retour.");
        }

        if (bindingResult.hasErrors()) {
            List<LivreDTO> livres = livreService.getAllLivres();
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("livres", livres);
            model.addAttribute("utilisateurs", utilisateurs);
            return "editEmprunt";
        }

        empruntService.createEmprunt(emprunt);
        return "redirect:/pages/emprunts";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateEmprunt/{id}")
    public String updateEmprunt(@PathVariable Long id, @ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être après la date de retour.");
        }

        if (emprunt.getUtilisateur() == null || emprunt.getUtilisateur().getId() == null) {
            bindingResult.rejectValue("utilisateur", "error.emprunt", "L'utilisateur doit être sélectionné.");
        }

        if (bindingResult.hasErrors()) {
            List<LivreDTO> livres = livreService.getAllLivres();
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("livres", livres);
            model.addAttribute("utilisateurs", utilisateurs);
            return "editEmprunt";
        }

        empruntService.updateEmprunt(id, emprunt);
        return "redirect:/pages/emprunts";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @GetMapping("/deleteEmprunt/{id}")
    public String deleteEmprunt(@PathVariable Long id) {
        empruntService.deleteEmprunt(id);
        return "redirect:/pages/emprunts";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/saveOrUpdateEmprunt")
    public String saveOrUpdateEmprunt(@ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être après la date de retour.");
        }

        if (emprunt.getUtilisateur() == null || emprunt.getUtilisateur().getId() == null) {
            bindingResult.rejectValue("utilisateur", "error.emprunt", "L'utilisateur doit être sélectionné.");
        }

        if (bindingResult.hasErrors()) {
            List<LivreDTO> livres = livreService.getAllLivres();
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("livres", livres);
            model.addAttribute("utilisateurs", utilisateurs);
            return "editEmprunt";
        }

        // Différencier entre la création et la mise à jour
        if (emprunt.getId() == null || emprunt.getId() == 0) {
            // Création
            empruntService.createEmprunt(emprunt);
        } else {
            // Mise à jour
            empruntService.updateEmprunt(emprunt.getId(), emprunt);
        }

        return "redirect:/pages/emprunts";
    }
}
