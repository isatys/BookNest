package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.DemandeEmpruntDTO;
import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.model.DemandeEmprunt;
import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.repository.DemandeEmpruntRepository;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.service.*;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
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

    @Autowired
    private DemandeEmpruntService demandeEmpruntService;

    @Autowired
    private DemandeEmpruntRepository demandeRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @GetMapping("/emprunts")
    public String getAllEmprunts( @RequestParam(value = "search", required = false) String search, Model model) {
        List<LivreDTO> livres = livreService.getAllLivres(); // Assurez-vous que cette méthode renvoie la liste des livres
        model.addAttribute("livres", livres);


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
        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        List<EmpruntDTO> emprunts;

        // Récupérer toutes les demandes d'emprunt
        List<DemandeEmpruntDTO> allDemandeEmprunts = demandeEmpruntService.getAllDemandeEmprunts();

        if (isAdmin) {
            model.addAttribute("demandes", allDemandeEmprunts);
            // Si l'utilisateur est admin, obtenir tous les emprunts
            emprunts = empruntService.getAllEmprunts();
        } else {
            // Pour les utilisateurs non administrateurs : filtrer les demandes d'emprunt par utilisateur
            List<DemandeEmpruntDTO> userDemandeEmprunts = allDemandeEmprunts.stream()
                    .filter(demande -> demande.getUtilisateur().getNom().equals(username))
                    .toList();
            if (userDemandeEmprunts.isEmpty()) {
                model.addAttribute("demandes", userDemandeEmprunts);
            }
            // Récupérer Utilisateur à partir de User
            UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(user.getUsername());
            if (utilisateur == null) {
                return "userEmprunts"; // Assurez-vous d'avoir une vue appropriée pour les emprunts de l'utilisateur
            }
            Long utilisateurId = utilisateur.getId();


            // Sinon, obtenir les emprunts de l'utilisateur spécifique
            emprunts = empruntService.getEmpruntsByUtilisateur(utilisateurId);
        }

        // Si une recherche est effectuée, rediriger vers la page des livres avec le paramètre de recherche
        if (search != null && !search.isEmpty()) {
            return "redirect:/pages/livres?search=" + search;
        }


        model.addAttribute("emprunts", emprunts);

        model.addAttribute("isAdmin", isAdmin);

        // Ajouter un nouvel emprunt par défaut pour éviter l'exception
        model.addAttribute("emprunt", new EmpruntDTO());

        // Ajouter les utilisateurs disponibles au modèle (si nécessaire)
        if (isAdmin) {
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("utilisateurs", utilisateurs);
        }

        // Retourner la vue appropriée
        return isAdmin ? "adminEmprunts" : "userEmprunts";
    }


    @GetMapping("/creer")
    public String creerDemandeForm(Model model) {
        model.addAttribute("livres", livreService.getAllLivres()); // Liste des livres pour le formulaire
        model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs()); // Liste des utilisateurs pour le formulaire
        model.addAttribute("demande", new DemandeEmprunt());
        return "userEmprunts"; // Nom du template Thymeleaf pour le formulaire
    }

    @PostMapping("/creer")
    public String creerDemande(@RequestParam Long livreId, @RequestParam LocalDate dateDebut, @RequestParam LocalDate dateFin) {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        UserDetails userDetails = (UserDetails) auth.getPrincipal();
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(userDetails.getUsername());


        demandeEmpruntService.creerDemandeEmprunt(livreId, utilisateur.getNom(), dateDebut, dateFin);
        return "redirect:/pages/emprunts";
    }

    @PostMapping("/accepterDemande")
    public String accepterDemande(@RequestParam Long demandeId) {
        DemandeEmprunt demande = demandeEmpruntService.findById(demandeId);
        Livre livre = demande.getLivre();

        // Vérification de la disponibilité du livre
        if (livreService.isLivreDisponible(livre.getId())) {
            demandeEmpruntService.accepterDemande(demandeId);

            // Envoyer un e-mail à l'utilisateur
            userService.sendEmail(demande.getUtilisateur().getEmail(), "Demande acceptée",
                    "Votre demande pour le livre " + livre.getTitre() + " a été acceptée.");

            // Supprimer la demande si elle est refusée
            demandeRepository.delete(demande);
        } else {
            refuserDemande(demandeId);
        }

        return "redirect:/pages/emprunts?info=DemandeAcceptee.";
    }

    @PostMapping("/refuserDemande")
    public String refuserDemande(@RequestParam Long demandeId) {
        DemandeEmprunt demande = demandeEmpruntService.findById(demandeId);
        demandeEmpruntService.refuserDemande(demandeId);

        // Envoyer un e-mail à l'utilisateur
        userService.sendEmail(demande.getUtilisateur().getEmail(), "Demande refusée",
                "Votre demande pour le livre " + demande.getLivre().getTitre() + " a été refusée car le livre n'est pas disponible.");

        // Supprimer la demande si elle est refusée
        demandeRepository.delete(demande);
        return "redirect:/pages/emprunts?info=DemandeRefusee.";
    }

    @PostMapping("/retour/{id}")
    public String retournerLivre(@PathVariable Long id, Model model) {
        try {
            empruntService.retournerLivre(id);
            model.addAttribute("infoMessage", "Le livre a été retourné avec succès.");
        } catch (RuntimeException e) {
            model.addAttribute("errorMessage", e.getMessage());
        }
        return "redirect:/pages/emprunts";
    }

    @GetMapping("/editEmprunt/{id}")
    public String showEditForm(@PathVariable("id") Long id, Model model) {
        Emprunt emprunt = empruntService.findById(id);
        model.addAttribute("emprunt", emprunt);
        model.addAttribute("livres", emprunt.getLivre());
        model.addAttribute("utilisateurs", emprunt.getUtilisateur());
        return "editEmprunt"; // assurez-vous que c'est le bon nom de vue
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
