package com.BookNest.BookNestCore.repository;

import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AuteurRepository  extends JpaRepository<Auteur, Long> {

    // Rechercher un auteur par son nom
    Auteur findByNom(String nom);
}
