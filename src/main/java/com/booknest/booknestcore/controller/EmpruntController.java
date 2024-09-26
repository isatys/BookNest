package com.booknest.booknestcore.controller;

import com.booknest.booknestcore.Enum.StatutDemande;
import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.dto.EmpruntDTO;
import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.dto.UtilisateurDTO;
import com.booknest.booknestcore.model.*;
import com.booknest.booknestcore.repository.DemandeEmpruntRepository;
import com.booknest.booknestcore.repository.EmpruntRepository;
import com.booknest.booknestcore.repository.LivreRepository;
import com.booknest.booknestcore.service.*;
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
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

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
    @Autowired
    private LivreRepository livreRepository;

    @GetMapping("/emprunts")
    public String getAllEmprunts(@RequestParam(value = "search", required = false) String search, Model model,RedirectAttributes redirectAttributes) {
        // Vérifiez si des messages d'erreur ou d'information sont disponibles
        if (redirectAttributes.getFlashAttributes().containsKey("errorMessage")) {
            model.addAttribute("errorMessage", redirectAttributes.getFlashAttributes().get("errorMessage"));
        }
        if (redirectAttributes.getFlashAttributes().containsKey("infoMessage")) {
            model.addAttribute("infoMessage", redirectAttributes.getFlashAttributes().get("infoMessage"));
        }

        List<LivreDTO> livres = livreService.getAllLivres();
        model.addAttribute("livres", livres);

        // Récupérer le nom de l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();

        // Récupérer l'utilisateur de la table User
        User user = userService.findByUsername(username);
        if (user == null) {
            model.addAttribute("errorMessage", "Utilisateur non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        boolean isAdmin = auth.getAuthorities().stream()
                .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN"));

        List<EmpruntDTO> emprunts;
        List<DemandeEmpruntDTO> allDemandeEmprunts = demandeEmpruntService.getAllDemandeEmprunts();

        if (isAdmin) {
            model.addAttribute("demandes", allDemandeEmprunts);
            emprunts = empruntService.getAllEmprunts();
        } else {
            List<DemandeEmpruntDTO> userDemandeEmprunts = allDemandeEmprunts.stream()
                    .filter(demande -> demande.getUtilisateur().getNom().equals(username))
                    .toList();
            model.addAttribute("demandes", userDemandeEmprunts);

            UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(user.getUsername());
            if (utilisateur == null) {
                return "userEmprunts"; // Assurez-vous d'avoir une vue appropriée pour les emprunts de l'utilisateur
            }
            Long utilisateurId = utilisateur.getId();
            emprunts = empruntService.getEmpruntsByUtilisateur(utilisateurId);
        }

        // Gérer la recherche
        if (search != null && !search.isEmpty()) {
            return "redirect:/pages/livres?search=" + search;
        }
        System.out.println("Model attributes: " + model.asMap());


        model.addAttribute("emprunts", emprunts);
        model.addAttribute("isAdmin", isAdmin);
        model.addAttribute("emprunt", new EmpruntDTO());

        if (isAdmin) {
            List<UtilisateurDTO> utilisateurs = utilisateurService.getAllUtilisateurs();
            model.addAttribute("utilisateurs", utilisateurs);
        }

        return isAdmin ? "adminEmprunts" : "userEmprunts";
    }

    @GetMapping("/creer")
    public String creerEmpruntForm(Model model) {
        model.addAttribute("livres", livreService.getAllLivres());
        model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
        return "adminEmprunts"; // Nom du template pour le formulaire
    }

    @PostMapping("/creer")
    public String creerEmprunt(@RequestParam Long livreId,
                               @RequestParam LocalDate dateEmprunt,
                               @RequestParam LocalDate dateRetour,
                               @RequestParam String utilisateurNom,
                               RedirectAttributes redirectAttributes) {
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(utilisateurNom);
        LocalDate aujourdHui = LocalDate.now();

        // Vérifier si la date d'emprunt est dans le passé
        if (dateEmprunt.isBefore(aujourdHui)) {
            redirectAttributes.addFlashAttribute("errorMessage", "La date d'emprunt ne peut pas être dans le passé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        if (utilisateur == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Utilisateur non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        // Récupérer le livre par son ID
        LivreDTO livre = livreService.getLivreById(livreId);
        if (livre == null) {
            redirectAttributes.addFlashAttribute("errorMessage", "Livre non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        // Créer un nouvel EmpruntDTO
        EmpruntDTO nouvelEmprunt = new EmpruntDTO();
        nouvelEmprunt.setLivre(livre);
        nouvelEmprunt.setDateEmprunt(dateEmprunt);
        nouvelEmprunt.setDateRetour(dateRetour);

        UtilisateurDTO utilisateurEmprunt = new UtilisateurDTO();
        utilisateurEmprunt.setId(utilisateur.getId());
        nouvelEmprunt.setUtilisateur(utilisateurEmprunt);

        // Appeler le service pour créer l'emprunt
        empruntService.createEmprunt(nouvelEmprunt);
        redirectAttributes.addFlashAttribute("infoMessage", "Emprunt créé avec succès.");

        return "redirect:/pages/emprunts";
    }



    @GetMapping("/creerDemande")
    public String creerDemandeEmpruntForm(Model model) {
        model.addAttribute("livres", livreService.getAllLivres());
        return "userEmprunts"; // Nom du template pour le formulaire
    }

    @PostMapping("/creerDemande")
    public String creerDemande(@RequestParam Long livreId, @RequestParam LocalDate dateEmprunt, @RequestParam LocalDate dateRetour, Model model) {
        // Récupérer le nom de l'utilisateur connecté
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = auth.getName();
// Vérifier si la date d'emprunt est dans le passé
        LocalDate aujourdHui = LocalDate.now();
        if (dateEmprunt.isBefore(aujourdHui) && dateRetour.isBefore(aujourdHui)) {
            model.addAttribute("errorMessage", "La date d'emprunt et de retour ne peut pas être dans le passé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(username);

        if (utilisateur == null) {
            model.addAttribute("errorMessage", "Utilisateur non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        // Récupérer le livre par son titre
        Livre livre = livreRepository.getById(livreId);
        if (livre == null) {
            model.addAttribute("errorMessage", "Livre non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        // Créer un nouvel EmpruntDTO
        DemandeEmpruntDTO nouvelleDemande = new DemandeEmpruntDTO();
        nouvelleDemande.setLivre(livre); // Affecter l'objet livre à l'emprunt
        nouvelleDemande.setDateEmprunt(dateEmprunt);
        nouvelleDemande.setDateRetour(dateRetour);

        // Initialiser le statut (par exemple, à EN_ATTENTE)
        nouvelleDemande.setStatut(StatutDemande.EN_ATTENTE);

        Utilisateur utilisateurEmprunt = new Utilisateur();
        utilisateurEmprunt.setId(utilisateur.getId()); // Utiliser l'ID de l'utilisateur récupéré
        nouvelleDemande.setUtilisateur(utilisateurEmprunt); // Affecter l'utilisateur


        // Appeler le service pour créer l'emprunt
        demandeEmpruntService.creerDemandeEmprunt(nouvelleDemande);// Si l'emprunt est créé avec succès, vous pouvez ajouter un message d'information ici
        model.addAttribute("infoMessage", "Emprunt créé avec succès.");


        return "redirect:/pages/emprunts";
    }

    @PostMapping("/accepterDemande")
    public String accepterDemande(@RequestParam Long demandeId, Model model) {
        DemandeEmprunt demande = demandeEmpruntService.findById(demandeId);
        if (demande == null) {
            model.addAttribute("errorMessage", "Demande non trouvée.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        Livre livre = demande.getLivre();
        if (livreService.isLivreDisponible(livre.getId())) {
            demandeEmpruntService.accepterDemande(demandeId);
            userService.sendEmail(demande.getUtilisateur().getEmail(), "Demande acceptée",
                    "Votre demande pour le livre " + livre.getTitre() + " a été acceptée.");
            demandeRepository.delete(demande);
        } else {
            refuserDemande(demandeId,model);
        }

        return "redirect:/pages/emprunts?info=DemandeAcceptee.";
    }

    @PostMapping("/refuserDemande")
    public String refuserDemande(@RequestParam Long demandeId, Model model) {
        DemandeEmprunt demande = demandeEmpruntService.findById(demandeId);
        if (demande == null) {
            model.addAttribute("errorMessage", "Demande non trouvée.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        demandeEmpruntService.refuserDemande(demandeId);
        userService.sendEmail(demande.getUtilisateur().getEmail(), "Demande refusée",
                "Votre demande pour le livre " + demande.getLivre().getTitre() + " a été refusée car le livre n'est pas disponible.");
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
        if (emprunt == null) {
            model.addAttribute("errorMessage", "Emprunt non trouvé.");
            return "redirect:/pages/emprunts"; // Vue d'erreur
        }

        model.addAttribute("emprunt", emprunt);
        model.addAttribute("livres", livreService.getAllLivres());
        model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
        return "editEmprunt";
    }

    @PreAuthorize("hasRole('ADMIN')")
    @PostMapping("/updateEmprunt/{id}")
    public String updateEmprunt(@PathVariable Long id, @ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (bindingResult.hasErrors()) {
            model.addAttribute("livres", livreService.getAllLivres());
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            return "editEmprunt";
        }

        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être après la date de retour.");
        }

        if (emprunt.getUtilisateur() == null || emprunt.getUtilisateur().getId() == null) {
            bindingResult.rejectValue("utilisateur", "error.emprunt", "L'utilisateur doit être sélectionné.");
        }

        if (bindingResult.hasErrors()) {
            model.addAttribute("livres", livreService.getAllLivres());
            model.addAttribute("utilisateurs", utilisateurService.getAllUtilisateurs());
            return "editEmprunt";
        }

        empruntService.updateEmprunt(id, emprunt);
        return "redirect:/pages/emprunts";
    }
}
