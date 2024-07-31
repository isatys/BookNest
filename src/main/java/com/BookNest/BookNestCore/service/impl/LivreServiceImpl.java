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

    /**
     * Récupère un livre par son identifiant.
     *
     * @param id l'identifiant du livre
     * @return le DTO du livre si trouvé, sinon null
     */
    @Override
    public LivreDTO getLivreById(Long id) {
        Livre livre = livreRepository.findById(id)
                .orElse(null);
        if (livre != null) {
            return LivreMapper.INSTANCE.livreToLivreDTO(livre);
        } else {
            return null;
        }
    }

    /**
     * Crée un nouveau livre.
     * TODO : Gérer le cas où l'auteur n'existe pas dans la base de données.
     *
     * @param livreDTO le DTO du livre à créer
     * @return le DTO du livre créé
     */
    @Override
    @Transactional
    public LivreDTO createLivre(LivreDTO livreDTO) {
        Livre livre;
        LivreDTO livreDto;
        // Si l'auteur n'existe pas, le créer avec le nom seulement
        if (livreDTO.getAuteur() != null || livreDTO.getAuteur().getNom() != null) {
            // Récupérer l'auteur du DTO
            Auteur auteur = auteurRepository.findByNom(livreDTO.getAuteur().getNom());
            livre = LivreMapper.INSTANCE.livreDTOToLivre(livreDTO);
            livre.setAuteur(auteur);
            livreDto = LivreMapper.INSTANCE.livreToLivreDTO(livreRepository.save(livre));
        } else {
            return null;
        }

        return livreDto;
    }

    /**
     * Supprime un livre par son identifiant.
     *
     * @param id l'identifiant du livre
     * @return un message de confirmation de suppression
     */
    @Override
    public String deleteLivre(Long id) {
        if (!livreRepository.existsById(id)) {
            throw new EntityNotFoundException("Livre avec l'ID " + id + " non trouvé.");
        }
        livreRepository.deleteById(id);
        return "Livre avec l'ID " + id + " a été supprimé avec succès.";
    }

    /**
     * Récupère tous les livres.
     *
     * @return une liste de DTOs de livres
     */
    @Override
    public List<LivreDTO> getAllLivres() {
        List<Livre> livres = livreRepository.findAll();
        if (livres.isEmpty()) {
            throw new EntityNotFoundException("Aucun livre trouvé.");
        }
        return livres.stream().map(LivreMapper.INSTANCE::livreToLivreDTO).collect(Collectors.toList());
    }

    /**
     * Met à jour un livre existant.
     *
     * @param id       l'identifiant du livre à mettre à jour
     * @param livreDTO le DTO du livre avec les nouvelles informations
     * @return le DTO du livre mis à jour si trouvé, sinon null
     */
    @Override
    public LivreDTO updateLivre(Long id, LivreDTO livreDTO) {
        Livre existingLivre = livreRepository.findById(id).orElse(null);
        if (existingLivre != null) {
            existingLivre.setTitre(livreDTO.getTitre());
            existingLivre.setGenre(livreDTO.getGenre());

            Livre updatedLivre = livreRepository.save(existingLivre);
            return LivreMapper.INSTANCE.livreToLivreDTO(updatedLivre);
        }
        return null;
    }

}
