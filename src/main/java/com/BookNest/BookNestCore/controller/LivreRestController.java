package com.BookNest.BookNestCore.controller;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.service.LivreService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.persistence.EntityNotFoundException;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * Contrôleur REST pour gérer les opérations sur les livres.
 */
@RestController
@RequestMapping("/api/livres")
public class LivreRestController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private LivreRepository livreRepository;


    // Ajoutez d'autres méthodes de contrôleur ici si nécessaire pour éditer, supprimer, etc.

    /**
     * Récupère tous les livres.
     *
     * @return ResponseEntity contenant la liste des LivreDTO si des livres sont trouvés, sinon une réponse 404
     */
    @Operation(summary = "Récupérer tous les livres", description = "Récupère la liste de tous les livres disponibles")
    @GetMapping
    public ResponseEntity<?> getAllLivresAPI() {
        try {
            List<LivreDTO> livres = livreService.getAllLivres();
            return ResponseEntity.ok(livres);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Aucun livre trouvé.");
        }
    }

    /**
     * Récupère un livre par son ID.
     *
     * @param id l'identifiant du livre à récupérer
     * @return ResponseEntity contenant le LivreDTO correspondant si trouvé, sinon une réponse 404
     */
    @Operation(summary = "Récupérer un livre par ID", description = "Récupère un livre spécifique en fonction de son identifiant")
    @ApiResponse(responseCode = "200", description = "Livre trouvé", content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivreDTO.class)))
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLivreById(@Parameter(description = "Identifiant du livre à récupérer") @PathVariable Long id) {
        LivreDTO livre = livreService.getLivreById(id);

        if (livre != null) {
            return ResponseEntity.status(HttpStatus.OK).body(livre);
        } else {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre avec l'ID " + id + " non trouvé.");
        }
    }

    /**
     * Crée un nouveau livre.
     *
     * @param livreDTO le DTO du livre à créer
     * @return ResponseEntity contenant le LivreDTO créé si succès, sinon une réponse avec un message d'erreur
     */
    @PostMapping
    @Operation(summary = "Créer un nouveau livre", description = "Ajoute un nouveau livre à la base de données avec les détails fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livre créé avec succès",
                    content = @Content(mediaType = "application/json", schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides",
                    content = @Content)
    })
    public ResponseEntity<?> createLivre(
            @Parameter(description = "Détails du livre à créer", required = true) @Valid @RequestBody LivreDTO livreDTO) {
        LivreDTO createdLivreDTO = livreService.createLivre(livreDTO);

        if (createdLivreDTO != null) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivreDTO);
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Échec de création du livre.");
        }
    }

    /**
     * Supprime un livre par son ID.
     *
     * @param id l'identifiant du livre à supprimer
     * @return ResponseEntity avec un message de succès si la suppression réussit, sinon une réponse 404 si le livre n'est pas trouvé
     */
    @Operation(summary = "Supprimer un livre", description = "Supprime un livre existant en fonction de son identifiant")
    @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivre(
            @Parameter(description = "Identifiant du livre à supprimer") @PathVariable Long id) {
        try {
            String message = livreService.deleteLivre(id);
            return ResponseEntity.ok(message);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre avec l'ID " + id + " non trouvé.");
        }
    }



    /**
     * Met à jour un livre existant.
     *
     * @param id       l'identifiant du livre à mettre à jour
     * @param livreDTO le DTO contenant les nouvelles informations du livre
     * @return ResponseEntity contenant le LivreDTO mis à jour si succès, sinon une réponse 404 si le livre n'est pas trouvé
     */
    @Operation(summary = "Mettre à jour un livre", description = "Met à jour les détails d'un livre existant en fonction de son identifiant")
    @ApiResponse(responseCode = "200", description = "Livre mis à jour avec succès", content = @Content(schema = @Schema(implementation = LivreDTO.class)))
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivre(
            @Parameter(description = "Identifiant du livre à mettre à jour") @PathVariable Long id,
            @Parameter(description = "Nouveaux détails du livre à mettre à jour", required = true) @Valid @RequestBody LivreDTO livreDTO) {
        try {
            LivreDTO updatedLivreDTO = livreService.updateLivre(id, livreDTO);
            if (updatedLivreDTO != null) {
                return ResponseEntity.ok(updatedLivreDTO);
            } else {
                return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre avec l'ID " + id + " non trouvé.");
            }
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Livre avec l'ID " + id + " non trouvé.");
        }
    }
}
