package com.BookNest.BookNestCore.repository;

import com.BookNest.BookNestCore.Enum.StatutDemande;
import com.BookNest.BookNestCore.model.DemandeEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeEmpruntRepository extends JpaRepository<DemandeEmprunt, Long> {
    List<DemandeEmprunt> findByStatut(StatutDemande statut);
}
