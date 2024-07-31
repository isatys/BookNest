package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.model.User;
import com.BookNest.BookNestCore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/pages")
public class AccueilController {
    @Autowired
    private UserService userService;

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