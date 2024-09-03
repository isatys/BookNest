package com.BookNest.BookNestCore.repository;


import com.BookNest.BookNestCore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

/**
 * Repository pour l'entité Livre.
 * Utilise JpaRepository pour les opérations CRUD de base.
 */
@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {

    List<Livre> findByGenre(String genre);

    @Query("SELECT DISTINCT l.genre FROM Livre l")
    List<String> findDistinctGenres();

    List<Livre> findByTitreContainingIgnoreCaseOrAuteurNomContainingIgnoreCaseOrGenreContainingIgnoreCase(
            String titre, String auteurNom, String genre);
}
