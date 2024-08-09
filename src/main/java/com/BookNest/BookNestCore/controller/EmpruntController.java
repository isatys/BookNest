package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.service.EmpruntService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
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

    @GetMapping("/emprunts")
    public String getAllEmprunts(Model model) {
        List<EmpruntDTO> emprunts = empruntService.getAllEmprunts();
        List<Livre> livres = empruntService.getAvailableBooks();
        model.addAttribute("emprunts", emprunts);
        model.addAttribute("livres", livres);
        model.addAttribute("emprunt", new EmpruntDTO()); // Uniformiser le nom de l'attribut
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
        return "editEmprunt";
    }

    @PostMapping("/createEmprunt")
    public String createEmprunt(@ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être supérieure à la date de retour.");
        }

        if (bindingResult.hasErrors()) {
            List<Livre> livres = empruntService.getAvailableBooks();
            model.addAttribute("livres", livres);
            return "editEmprunt"; // Retourne la vue avec les erreurs de validation
        }

        empruntService.createEmprunt(emprunt);
        return "redirect:/pages/emprunts";
    }

    @GetMapping("/deleteEmprunt/{id}")
    public String deleteEmprunt(@PathVariable Long id) {
        empruntService.deleteEmprunt(id);
        return "redirect:/pages/emprunts";
    }

    @GetMapping("/editEmprunt/{id}")
    public String showEditForm(@PathVariable Long id, Model model) {
        EmpruntDTO emprunt = empruntService.getEmpruntById(id);
        List<Livre> livres = empruntService.getAvailableBooks();
        model.addAttribute("emprunt", emprunt); // Utiliser le même nom d'attribut
        model.addAttribute("livres", livres);
        return "editEmprunt";
    }

    @PostMapping("/updateEmprunt/{id}")
    public String updateEmprunt(@PathVariable Long id, @ModelAttribute("emprunt") @Valid EmpruntDTO emprunt, BindingResult bindingResult, Model model) {
        if (emprunt.getDateEmprunt().isAfter(emprunt.getDateRetour())) {
            bindingResult.rejectValue("dateEmprunt", "error.emprunt", "La date d'emprunt ne peut pas être supérieure à la date de retour.");
        }

        if (bindingResult.hasErrors()) {
            List<Livre> livres = empruntService.getAvailableBooks();
            model.addAttribute("livres", livres);
            return "editEmprunt"; // Retourne la vue avec les erreurs de validation
        }

        empruntService.updateEmprunt(id, emprunt);
        return "redirect:/pages/emprunts";
    }
}
