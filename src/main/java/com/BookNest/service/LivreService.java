package com.BookNest.service;

import com.BookNest.model.Livre;

import java.util.List;

public interface LivreService {
        List<Livre> getAllLivres();
        Livre getLivreById(Long id);
        Livre saveLivre(Livre livre);
        void deleteLivre(Long id);

    List<Livre> getLivresByAuteur(String nomAuteur);

    List<Livre> getLivresByGenre(String genre);
}


