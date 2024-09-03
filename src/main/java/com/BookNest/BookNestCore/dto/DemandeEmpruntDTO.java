package com.BookNest.BookNestCore.dto;

import com.BookNest.BookNestCore.Enum.StatutDemande;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.Utilisateur;
import jakarta.persistence.*;

import java.time.LocalDateTime;

public class DemandeEmpruntDTO {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    private Livre livre;

    @ManyToOne
    private Utilisateur utilisateur;

    private LocalDateTime dateDemande;

    @Enumerated(EnumType.STRING)
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

    public LocalDateTime getDateDemande() {
        return dateDemande;
    }

    public void setDateDemande(LocalDateTime dateDemande) {
        this.dateDemande = dateDemande;
    }

    public StatutDemande getStatut() {
        return statut;
    }

    public void setStatut(StatutDemande statut) {
        this.statut = statut;
    }
}
