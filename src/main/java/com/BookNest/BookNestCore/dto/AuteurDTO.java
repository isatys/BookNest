package com.BookNest.BookNestCore.dto;

import com.BookNest.BookNestCore.model.Livre;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class AuteurDTO {

    @Schema(hidden = true)
    private Long id;
    private String nom;
    private String biographie;
    private List<LivreDTO> livres;

    // Getters and setters

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }

    public String getBiographie() {
        return biographie;
    }

    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    public List<LivreDTO> getLivres() {
        return livres;
    }

    public void setLivres(List<LivreDTO> livres) {
        this.livres = livres;
    }
}
