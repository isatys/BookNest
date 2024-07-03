package com.BookNest.BookNestCore.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;

public class UtilisateurDTO {
    @Schema(hidden = true)
    private Long id;
    private String nom;
    private String adresse;

    private List<EmpruntDTO> emprunts;
}
