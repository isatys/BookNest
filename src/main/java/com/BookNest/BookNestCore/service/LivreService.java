package com.BookNest.BookNestCore.service;


import com.BookNest.BookNestCore.dto.LivreDTO;

public interface LivreService {


    LivreDTO getLivreById(Long id);

    LivreDTO createLivre(LivreDTO livreDTO);

    /*List<LivreDTO> getAllLivres();


    LivreDTO updateLivre(Long id, LivreDTO livreDTO);*/

    String deleteLivre(Long id);
}
