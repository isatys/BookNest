package com.BookNest.BookNestCore.service;


import com.BookNest.BookNestCore.dto.LivreDTO;

public interface LivreService {


    LivreDTO getLivreById(Long id);


    /*List<LivreDTO> getAllLivres();

    LivreDTO createLivre(LivreDTO livreDTO);

    LivreDTO updateLivre(Long id, LivreDTO livreDTO);

    void deleteLivre(Long id);*/
}
