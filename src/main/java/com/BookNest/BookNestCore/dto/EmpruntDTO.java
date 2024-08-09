package com.BookNest.BookNestCore.dto;


import java.time.LocalDate;

public class EmpruntDTO {

    private Long id; // ou int, ou String selon vos besoins

    private LivreDTO livre;

    private UtilisateurDTO utilisateur;

    private LocalDate dateEmprunt;
    private LocalDate dateRetour;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LivreDTO getLivre() {
        return livre;
    }

    public void setLivre(LivreDTO livre) {
        this.livre = livre;
    }

    public UtilisateurDTO getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(UtilisateurDTO utilisateur) {
        this.utilisateur = utilisateur;
    }

    public LocalDate getDateEmprunt() {
        return dateEmprunt;
    }

    public void setDateEmprunt(LocalDate dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    public LocalDate getDateRetour() {
        return dateRetour;
    }

    public void setDateRetour(LocalDate dateRetour) {
        this.dateRetour = dateRetour;
    }
}
