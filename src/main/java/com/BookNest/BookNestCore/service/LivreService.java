package com.BookNest.BookNestCore.service;


import com.BookNest.BookNestCore.dto.LivreDTO;

import java.util.List;

public interface LivreService {


    LivreDTO getLivreById(Long id);

    LivreDTO createLivre(LivreDTO livreDTO);

    List<LivreDTO> getAllLivres();

    LivreDTO updateLivre(Long id, LivreDTO livreDTO);

    String deleteLivre(Long id);
}
