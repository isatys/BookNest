package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.LivreService;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/pages")
public class AccueilController {

    @Autowired
    private UserService userService;

    @Autowired
    private LivreService bookService; // Injectez le service BookService

    @GetMapping("/accueil")
    public String accueilPage(@RequestParam(value = "search", required = false) String search, Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails userDetails) {
                model.addAttribute("username", userDetails.getUsername());
                model.addAttribute("isAdmin", userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
                model.addAttribute("isUser", userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_USER")));
            } else if (principal instanceof String username) {
                User user = userService.findByUsername(username);
                if (user != null) {
                    model.addAttribute("username", username);
                    model.addAttribute("isAdmin", isAdmin(user));
                    model.addAttribute("isUser", isUser(user));
                }
            }
        }

        // Si une recherche est effectuée, rediriger vers la page des livres avec le paramètre de recherche
        if (search != null && !search.isEmpty()) {
            return "redirect:/pages/livres?search=" + search;
        }

        // Récupération des Best-sellers
        List<Livre> bestSellers = bookService.getBestSellers();
        model.addAttribute("bestSellers", bestSellers);

        // Récupération des Nouveautés
        List<Livre> newArrivals = bookService.getNewArrivals();
        model.addAttribute("newArrivals", newArrivals);

        // Récupération des Livres les plus empruntés
        List<Livre> mostBorrowedBooks = bookService.getMostBorrowedBooks();
        model.addAttribute("mostBorrowedBooks", mostBorrowedBooks);

        // Récupération des Recommandations pour l'utilisateur
        if (authentication != null && authentication.isAuthenticated()) {
            String username = authentication.getName();
            User user = userService.findByUsername(username);
            if (user != null) {
                List<Livre> recommendedBooks = bookService.getRecommendedBooksForUser();
                model.addAttribute("recommendedBooks", recommendedBooks);
            }
        }

        return "accueil"; // Le nom du template Thymeleaf
    }

    private boolean isAdmin(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));
    }

    private boolean isUser(User user) {
        return user.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_USER"));
    }
}
