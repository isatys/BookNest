package com.BookNest.BookNestCore.repository;

import com.BookNest.BookNestCore.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByUtilisateurId(Long utilisateurId);
}
