package com.booknest.booknestcore.service;

import com.booknest.booknestcore.dto.EmpruntDTO;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.model.Livre;

import java.util.List;

public interface EmpruntService {
    List<EmpruntDTO> getAllEmprunts();
    EmpruntDTO getEmpruntById(Long id);
    void createEmprunt(EmpruntDTO empruntDTO);
    void updateEmprunt(Long id, EmpruntDTO empruntDTO);
    void deleteEmprunt(Long id);

    List<Livre> getAvailableBooks();


    List<EmpruntDTO> getEmpruntsByUtilisateur(Long utilisateurId);

    Emprunt findById(Long id);

    void retournerLivre(Long empruntId);

}
