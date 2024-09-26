package com.booknest.booknestcore.service;

import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.model.DemandeEmprunt;

import java.time.LocalDate;
import java.util.List;

public interface DemandeEmpruntService {

    void creerDemandeEmprunt(DemandeEmpruntDTO demandeEmpruntDTO);

    List<DemandeEmprunt> getDemandesEnAttente();

    List<DemandeEmpruntDTO> getAllDemandeEmprunts();

    void accepterDemande(Long demandeId);

    void refuserDemande(Long demandeId);


    DemandeEmprunt findById(Long demandeId);
}
