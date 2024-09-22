package com.booknest.booknestcore.controller;

import com.booknest.booknestcore.model.Role;
import com.booknest.booknestcore.model.User;
import com.booknest.booknestcore.model.Utilisateur;
import com.booknest.booknestcore.repository.RoleRepository;
import com.booknest.booknestcore.repository.UtilisateurRepository;
import com.booknest.booknestcore.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class SignUpController {
    @Autowired
    private UserService userService;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    /**
     * Affiche le formulaire d'inscription.
     *
     * @param model le modèle pour passer des données à la vue.
     * @return le nom du template Thymeleaf pour la page d'inscription.
     */
    @GetMapping("/signup")
    public String showSignupForm(Model model) {
        model.addAttribute("user", new User());
        return "signup";
    }

    /**
     * Gère la soumission du formulaire d'inscription.
     *
     * @param username l'utilisateur à enregistrer.
     * @return une redirection vers la page de connexion après une inscription réussie.
     */
    @PostMapping("/signup")
    public String signUp(@RequestParam String username, @RequestParam String password,@RequestParam String  email, Model model) {
        // Vérifier si l'utilisateur existe déjà
        User existingUser = userService.findByUsername(username);
        if (existingUser != null) {
            model.addAttribute("error", "Username already exists. Please choose a different username.");
            return "signup";
        }

        // Créer un nouvel utilisateur
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); // Encodage du mot de passe
        newUser.setEmail(email); // Assurez-vous que l'email est défini

        // Ajouter le rôle USER par défaut
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseThrow(() -> new RuntimeException("User role not found"));
        newUser.getRoles().add(userRole);

        // Enregistrer l'utilisateur
        userService.saveUser(newUser);

        // Vérifiez si l'utilisateur est un admin ou non
        boolean isAdmin = newUser.getRoles().stream()
                .anyMatch(role -> role.getName().equals("ROLE_ADMIN"));

        if (!isAdmin) {
            // Créer un utilisateur pour la table 'utilisateur'
            Utilisateur utilisateur = new Utilisateur();
            utilisateur.setNom(username);
            utilisateur.setEmail(email);

            // Enregistrer l'utilisateur dans la table 'utilisateur'
            Utilisateur savedUtilisateur  = utilisateurRepository.save(utilisateur);
            System.out.println("Saved Utilisateur: " + savedUtilisateur); // Debugging

        }

        return "redirect:/login"; // Rediriger vers la page de connexion après inscription réussie
    }

}
