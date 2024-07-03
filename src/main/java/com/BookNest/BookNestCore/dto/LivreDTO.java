package com.BookNest.BookNestCore.dto;


import com.BookNest.BookNestCore.model.Auteur;
import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Détails sur le livre")
public class LivreDTO {

    @Schema(hidden = true)
    private Long id;

    @Schema(description = "Titre du livre", example = "Le Grand Gatsby")
    private String titre;

    @Schema(description = "Genre du livre", example = "Fiction")
    private String genre;

    @Schema(description = "Détails sur l'auteur du livre")
    private AuteurDTO auteur;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public AuteurDTO getAuteur() {
        return auteur;
    }

    public void setAuteur(AuteurDTO auteur) {
        this.auteur = auteur;
    }

}

