package com.BookNest.BookNestCore.repository;

import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Auteur.
 * Utilise JpaRepository pour les opérations CRUD de base.
 */
@Repository
public interface AuteurRepository extends JpaRepository<Auteur, Long> {

    /**
     * Recherche un auteur par son nom.
     *
     * @param nom le nom de l'auteur à rechercher
     * @return l'auteur correspondant au nom donné
     */
    Auteur findByNom(String nom);
}