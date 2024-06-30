package com.BookNest.BookNestCore.service;


import com.BookNest.BookNestCore.dto.LivreDTO;

import java.util.List;

public interface LivreService {

    List<LivreDTO> getAllLivres();

    LivreDTO getLivreById(Long id);

    LivreDTO createLivre(LivreDTO livreDTO);

    LivreDTO updateLivre(Long id, LivreDTO livreDTO);

    void deleteLivre(Long id);
}
