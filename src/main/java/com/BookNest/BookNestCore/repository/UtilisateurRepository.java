package com.BookNest.BookNestCore.repository;

import com.BookNest.BookNestCore.model.Utilisateur;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UtilisateurRepository extends JpaRepository<Utilisateur, Long> {
    // Méthode pour trouver un utilisateur par nom
    Optional<Utilisateur> findByNom(String nom);
}
