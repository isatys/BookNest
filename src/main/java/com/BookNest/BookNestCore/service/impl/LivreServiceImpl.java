package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.mapper.LivreMapper;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.service.LivreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;

    @Autowired
    public LivreServiceImpl(LivreRepository livreRepository) {
        this.livreRepository = livreRepository;
    }


    @Override
    public LivreDTO getLivreById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElse(null);
        if(livre != null){
            return LivreMapper.INSTANCE.livreToLivreDTO(livre);
        }else{
            return null;
        }

    }
/*
 @Override
    public List<LivreDTO> getAllLivres() {
        List<Livre> livres = livreRepository.findAll();
        return livres.stream()
                .map(LivreMapper.INSTANCE::livreToLivreDTO)
                .collect(Collectors.toList());
    }

    @Override
    public LivreDTO createLivre(LivreDTO livreDTO) {
        Livre livre = LivreMapper.INSTANCE.livreDTOToLivre(livreDTO);
        Livre savedLivre = livreRepository.save(livre);
        return LivreMapper.INSTANCE.livreToLivreDTO(savedLivre);
    }

    @Override
    public LivreDTO updateLivre(Long id, LivreDTO livreDTO) {
        Livre existingLivre = livreRepository.findById(id).orElse(null);
        if (existingLivre != null) {
            existingLivre.setTitre(livreDTO.getTitre());
            existingLivre.setGenre(livreDTO.getGenre());
            // Mise à jour des autres champs si nécessaire

            Livre updatedLivre = livreRepository.save(existingLivre);
            return LivreMapper.INSTANCE.livreToLivreDTO(updatedLivre);
        }
        return null;
    }

    @Override
    public void deleteLivre(Long id) {
        livreRepository.deleteById(id);
    }*/

}
