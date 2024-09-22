package com.booknest.booknestcore.repository;


import com.booknest.booknestcore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

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

    Page<Livre> findByGenre(String genre, Pageable pageable);
    Page<Livre> findByTitreContainingIgnoreCase(String search, Pageable pageable);
    Page<Livre> findAll(Pageable pageable); // Pour la pagination sans filtre

}
