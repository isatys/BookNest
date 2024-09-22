package com.booknest.booknestcore.repository;

import com.booknest.booknestcore.Enum.StatutDemande;
import com.booknest.booknestcore.model.DemandeEmprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DemandeEmpruntRepository extends JpaRepository<DemandeEmprunt, Long> {
    List<DemandeEmprunt> findByStatut(StatutDemande statut);
}
