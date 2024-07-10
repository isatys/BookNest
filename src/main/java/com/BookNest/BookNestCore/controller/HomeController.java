package com.BookNest.BookNestCore.controller;


import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    @GetMapping("/")
    public String home() {
        return "index"; // Renvoie le template Thymeleaf index.html pour la page d'accueil
    }
}