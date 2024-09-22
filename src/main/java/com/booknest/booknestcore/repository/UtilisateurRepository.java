package com.booknest.booknestcore.repository;

import com.booknest.booknestcore.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Méthode pour trouver un utilisateur par nom
    Optional<Utilisateur> findByNom(String nom);
}
