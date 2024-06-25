package com.BookNest.repository;


import com.BookNest.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
    List<Livre> findByAuteurNom(String nomAuteur);

    List<Livre> findByGenre(String genre);
}
