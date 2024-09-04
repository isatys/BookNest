package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.dto.DemandeEmpruntDTO;
import com.BookNest.BookNestCore.model.DemandeEmprunt;

import java.time.LocalDate;
import java.util.List;

public interface DemandeEmpruntService {

    void creerDemandeEmprunt(Long livreId, String nom,LocalDate dateEmprunt, LocalDate dateRetour);

    List<DemandeEmprunt> getDemandesEnAttente();

    List<DemandeEmpruntDTO> getAllDemandeEmprunts();

    void accepterDemande(Long demandeId);

    void refuserDemande(Long demandeId);


    DemandeEmprunt findById(Long demandeId);
}
