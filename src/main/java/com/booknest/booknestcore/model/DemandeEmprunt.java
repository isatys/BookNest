package com.booknest.booknestcore.model;


import com.booknest.booknestcore.Enum.StatutDemande;
import jakarta.persistence.*;

import java.time.LocalDate;

@Entity
@Table(name = "demande_emprunt")
public class DemandeEmprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "livre_id", nullable = false)
    private Livre livre;

    @ManyToOne
    @JoinColumn(name = "utilisateur_id", nullable = false)
    private Utilisateur utilisateur;

    /** La date à laquelle l'emprunt a été effectué. */
    @Column(name = "date_emprunt", nullable = false)
    private LocalDate dateEmprunt;

    /** La date à laquelle le livre doit être retourné. */
    @Column(name = "date_retour", nullable = false)
    private LocalDate dateRetour;

    @Enumerated(EnumType.STRING)
    @Column(name = "statut", nullable = false)
    private StatutDemande statut;

    @PrePersist
    protected void onCreate() {
        if (this.dateEmprunt == null) {
            this.dateEmprunt = LocalDate.now(); // Assignez une valeur par défaut si nécessaire
        }
        if (this.dateRetour == null) {
            this.dateRetour = LocalDate.now(); // Assignez une valeur par défaut si nécessaire
        }
    }

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

    // Getters and Setters
    // Constructors
}
