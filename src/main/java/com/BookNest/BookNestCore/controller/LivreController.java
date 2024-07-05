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


@RestController
@RequestMapping("/livres")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @Autowired
    private LivreRepository livreRepository;



    @Operation(summary = "Récupérer un livre par ID", description = "Récupère un livre spécifique en fonction de son identifiant")
    @ApiResponse(responseCode = "200", description = "Livre trouvé", content = @Content(mediaType = "application/json",schema = @Schema(implementation = LivreDTO.class)))
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @GetMapping("/{id}")
    public ResponseEntity<?> getLivreById(@Parameter(description = "Identifiant du livre à mettre à jour") @PathVariable Long id) {
        LivreDTO livre = livreService.getLivreById(id);

        if (livre != null) {
            return ResponseEntity.status(HttpStatus.OK).body(livre);
        } else {
            // Si l'utilisateur n'est pas trouvé, retourne une réponse HTTP 500 avec un message personnalisé
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("ID non trouvé dans la base de données pour l'ID : " + id);
        }
    }

    @PostMapping
    @Operation(summary = "Créer un nouveau livre", description = "Ajoute un nouveau livre à la base de données avec les détails fournis.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Livre créé avec succès",
                    content = @Content(mediaType = "application/json",
                            schema = @Schema(implementation = LivreDTO.class))),
            @ApiResponse(responseCode = "400", description = "Données de requête invalides",
                    content = @Content)
    })    public ResponseEntity<?> createLivre(
            @Parameter(description = "Détails du livre à créer", required = true) @Valid @RequestBody LivreDTO livreDTO) {
        LivreDTO createdLivreDTO = livreService.createLivre(livreDTO);
        if (createdLivreDTO.getAuteur() != null  && createdLivreDTO.getGenre()!= null || createdLivreDTO.getGenre().isEmpty() && createdLivreDTO.getTitre()!= null || createdLivreDTO.getTitre().isEmpty()) {
            return ResponseEntity.status(HttpStatus.CREATED).body(createdLivreDTO);

        }else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Il manque des données pour l'Id: " + createdLivreDTO.getId());
        }
    }

    @Operation(summary = "Supprimer un livre", description = "Supprime un livre existant en fonction de son identifiant")
    @ApiResponse(responseCode = "204", description = "Livre supprimé avec succès")
    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteLivre(
            @Parameter(description = "Identifiant du livre à supprimer") @PathVariable Long id) {
        try {
            String message = livreService.deleteLivre(id);
            return ResponseEntity.ok(message);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }    }

    @Operation(summary = "Récupérer tous les livres", description = "Récupère la liste de tous les livres disponibles")
    @GetMapping
    public ResponseEntity<?> getAllLivres() {
        try {
            List<LivreDTO> livres = livreService.getAllLivres();
            return ResponseEntity.ok(livres);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }

    }

    @Operation(summary = "Mettre à jour un livre", description = "Met à jour les détails d'un livre existant en fonction de son identifiant")
    @ApiResponse(responseCode = "200", description = "Livre mis à jour avec succès", content = @Content(schema = @Schema(implementation = LivreDTO.class)))
    @ApiResponse(responseCode = "404", description = "Livre non trouvé")
    @PutMapping("/{id}")
    public ResponseEntity<?> updateLivre(
            @Parameter(description = "Identifiant du livre à mettre à jour", example = "1") @PathVariable Long id,
            @Parameter(description = "Nouveaux détails du livre à mettre à jour", required = true) @Valid @RequestBody LivreDTO livreDTO) {
        try {
            LivreDTO updatedLivreDTO = livreService.updateLivre(id, livreDTO);
            return ResponseEntity.ok(updatedLivreDTO);
        } catch (EntityNotFoundException ex) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ex.getMessage());
        }
    }


}
