package com.BookNest.service.impl;

import com.BookNest.model.Livre;
import com.BookNest.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.BookNest.repository.LivreRepository;

import java.util.List;

@Service
public class LivreServiceImpl implements LivreService {

    @Autowired
    private LivreRepository livreRepository;

    @Autowired
    public LivreServiceImpl(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }
    @Override
    public List<Livre> getAllLivres() {
        return livreRepository.findAll();
    }

    @Override
    public Livre getLivreById(Long id) {
        return livreRepository.findById(id).orElse(null);
    }

    @Override
    public Livre saveLivre(Livre livre) {
        return livreRepository.save(livre);
    }

    @Override
    public void deleteLivre(Long id) {
        livreRepository.deleteById(id);
    }

    @Override
    public List<Livre> getLivresByAuteur(String nomAuteur) {
        // Implémentez la logique pour récupérer les livres par auteur
        return livreRepository.findByAuteurNom(nomAuteur);
    }

    @Override
    public List<Livre> getLivresByGenre(String genre) {
        // Implémentez la logique pour récupérer les livres par genre
        return livreRepository.findByGenre(genre);
    }
}
