package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.dto.LivreDTO;

import java.util.List;

/**
 * Interface pour le service Livre.
 * Fournit les méthodes de gestion des livres.
 */
public interface LivreService {

    /**
     * Récupère un livre par son identifiant.
     *
     * @param id l'identifiant du livre
     * @return le DTO du livre si trouvé
     */
    LivreDTO getLivreById(Long id);

    /**
     * Crée un nouveau livre.
     *
     * @param livreDTO le DTO du livre à créer
     * @return le DTO du livre créé
     */
    LivreDTO createLivre(LivreDTO livreDTO);

    /**
     * Récupère tous les livres.
     *
     * @return une liste de DTOs de livres
     */
    List<LivreDTO> getAllLivres();

    /**
     * Met à jour un livre existant.
     *
     * @param id       l'identifiant du livre à mettre à jour
     * @param livreDTO le DTO du livre avec les nouvelles informations
     * @return le DTO du livre mis à jour
     */
    LivreDTO updateLivre(Long id, LivreDTO livreDTO);

    /**
     * Supprime un livre par son identifiant.
     *
     * @param id l'identifiant du livre
     * @return un message de confirmation de suppression
     */
    String deleteLivre(Long id);
}
