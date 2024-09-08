package com.BookNest.BookNestCore.dto;

import com.BookNest.BookNestCore.Enum.StatutDemande;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.Utilisateur;
import jakarta.persistence.*;

import java.time.LocalDate;
import java.time.LocalDateTime;

public class DemandeEmpruntDTO {

    private Long id;

    private Livre livre;

    private Utilisateur utilisateur;

    private LocalDate dateEmprunt;

    private LocalDate dateRetour;

    private StatutDemande statut;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Livre getLivre() {
        return livre;
    }

    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
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
