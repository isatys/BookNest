package com.BookNest.BookNestCore.dto;


import com.BookNest.BookNestCore.model.Auteur;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Schema(description = "Détails sur le livre")
public class LivreDTO {

    @Schema(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Schema(description = "Titre du livre", example = "Le Grand Gatsby")
    private String titre;

    @Schema(description = "Genre du livre", example = "Fiction")
    private String genre;

    @Schema(description = "Id du livre")
    private Long auteurId;

    public LivreDTO() {}

    // Getters and setters

    public LivreDTO(Long id, String titre) {
        this.id = id;
        this.titre = titre;
    }

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

    public Long getAuteurId() {
        return auteurId;
    }

    public void setAuteurId(Long auteurId) {
        this.auteurId = auteurId;
    }
}

