package com.BookNest.BookNestCore.controller;

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

import java.util.List;

@Controller
@RequestMapping("/pages")
public class AccueilController {

    @Autowired
    private UserService userService;

    @Autowired
    private LivreService bookService; // Injectez le service BookService

    @GetMapping("/accueil")
    public String accueilPage(Model model, Authentication authentication) {
        if (authentication != null && authentication.isAuthenticated()) {
            Object principal = authentication.getPrincipal();
            if (principal instanceof UserDetails) {
                UserDetails userDetails = (UserDetails) principal;
                model.addAttribute("username", userDetails.getUsername());
                model.addAttribute("isAdmin", userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_ADMIN")));
                model.addAttribute("isUser", userDetails.getAuthorities().stream()
                        .anyMatch(role -> role.getAuthority().equals("ROLE_USER")));
            } else if (principal instanceof String) {
                String username = (String) principal;
                User user = userService.findByUsername(username);
                if (user != null) {
                    model.addAttribute("username", username);
                    model.addAttribute("isAdmin", isAdmin(user));
                    model.addAttribute("isUser", isUser(user));
                }
            }
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
