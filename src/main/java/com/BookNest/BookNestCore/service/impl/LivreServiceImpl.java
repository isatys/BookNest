package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.mapper.LivreMapper;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.repository.AuteurRepository;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.service.LivreService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class LivreServiceImpl implements LivreService {

    private final LivreRepository livreRepository;
    private final AuteurRepository auteurRepository;

    @Autowired
    public LivreServiceImpl(LivreRepository livreRepository, AuteurRepository auteurRepository) {
        this.livreRepository = livreRepository;
        this.auteurRepository = auteurRepository;
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
    //TODO gerer le cas ou les auteurs n'existe pas dans la base de donnees
    @Override
    @Transactional
    public LivreDTO createLivre(LivreDTO livreDTO) {
        Livre livre ;
        // Récupérer l'auteur du DTO
        Auteur auteur = auteurRepository.findByNom(livreDTO.getAuteur().getNom());
        // Si l'auteur n'existe pas, le créer avec le nom seulement
        if(livreDTO.getAuteur().getNom() != null){
            livre = LivreMapper.INSTANCE.livreDTOToLivre(livreDTO);
            livre.setAuteur(auteur);
        }else{
            return null;
        }


        return LivreMapper.INSTANCE.livreToLivreDTO(livreRepository.save(livre));
    }
    @Override
    public String deleteLivre(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new EntityNotFoundException("Livre avec l'ID " + id + " non trouvé.");
        }
        livreRepository.deleteById(id);
        return "Livre avec l'ID " + id + " a été supprimé avec succès.";
    }

 @Override
    public List<LivreDTO> getAllLivres() {
     List<Livre> livres = livreRepository.findAll();
     if (livres.isEmpty()) {
         throw new EntityNotFoundException("Aucun livre trouvé.");
     }
     return livres.stream().map(LivreMapper.INSTANCE::livreToLivreDTO).collect(Collectors.toList());
 }
}

/*

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

    */


