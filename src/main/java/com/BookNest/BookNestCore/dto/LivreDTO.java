package com.BookNest.BookNestCore.dto;


import com.BookNest.BookNestCore.model.Auteur;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

@Schema(description = "Détails sur le livre")
public class LivreDTO {

    private Long id;

    private String titre;

    private String genre;

    private String nomAuteur;

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

    public String getNomAuteur() {
        return nomAuteur;
    }

    public void setNomAuteur(String nomAuteur) {
        this.nomAuteur = nomAuteur;
    }
}

