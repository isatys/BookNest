package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.model.Livre;

import java.util.List;

public interface EmpruntService {
    List<EmpruntDTO> getAllEmprunts();
    EmpruntDTO getEmpruntById(Long id);
    void createEmprunt(EmpruntDTO empruntDTO);
    void updateEmprunt(Long id, EmpruntDTO empruntDTO);
    void deleteEmprunt(Long id);

    List<Livre> getAvailableBooks();
}
