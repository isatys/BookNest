package com.BookNest.BookNestCore.repository;


import com.BookNest.BookNestCore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

/**
 * Repository pour l'entité Livre.
 * Utilise JpaRepository pour les opérations CRUD de base.
 */
@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
}
