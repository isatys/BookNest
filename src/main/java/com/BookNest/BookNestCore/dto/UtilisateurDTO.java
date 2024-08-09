package com.BookNest.BookNestCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;

import java.util.List;

public class UtilisateurDTO {

    @Schema(hidden = true)
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String nom;
    public UtilisateurDTO() {}

    public UtilisateurDTO(Long id, String nom) {
        this.id = id;
        this.nom = nom;
    }


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


}
