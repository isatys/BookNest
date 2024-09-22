package com.booknest.booknestcore.repository;

import com.booknest.booknestcore.model.Emprunt;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface EmpruntRepository extends JpaRepository<Emprunt, Long> {
    List<Emprunt> findByUtilisateurId(Long utilisateurId);

    // Trouver tous les emprunts actifs pour un livre donné (non retourné)
    List<Emprunt> findByLivreIdAndDateRetourIsNull(Long livreId);
    List<Emprunt> findBydateRetour(LocalDate dateRetour);
}
