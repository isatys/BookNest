package com.BookNest.BookNestCore.model;

import jakarta.persistence.*;
import java.util.Date;

/**
 * Représente un emprunt d'un livre par un utilisateur.
 */
@Entity
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** La date à laquelle l'emprunt a été effectué. */
    private Date dateEmprunt;

    /** La date à laquelle le livre doit être retourné. */
    private Date dateRetour;

    /** Le livre emprunté. */
    @ManyToOne
    private Livre livre;

    /** L'utilisateur qui a effectué l'emprunt. */
    @ManyToOne
    private Utilisateur utilisateur;

    /**
     * Obtient l'identifiant de l'emprunt.
     * @return l'identifiant de l'emprunt
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'emprunt.
     * @param id l'identifiant de l'emprunt à définir
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient la date d'emprunt du livre.
     * @return la date d'emprunt du livre
     */
    public Date getDateEmprunt() {
        return dateEmprunt;
    }

    /**
     * Définit la date d'emprunt du livre.
     * @param dateEmprunt la date d'emprunt du livre à définir
     */
    public void setDateEmprunt(Date dateEmprunt) {
        this.dateEmprunt = dateEmprunt;
    }

    /**
     * Obtient la date de retour prévue du livre.
     * @return la date de retour prévue du livre
     */
    public Date getDateRetour() {
        return dateRetour;
    }

    /**
     * Définit la date de retour prévue du livre.
     * @param dateRetour la date de retour prévue du livre à définir
     */
    public void setDateRetour(Date dateRetour) {
        this.dateRetour = dateRetour;
    }

    /**
     * Obtient le livre emprunté.
     * @return le livre emprunté
     */
    public Livre getLivre() {
        return livre;
    }

    /**
     * Définit le livre emprunté.
     * @param livre le livre emprunté à définir
     */
    public void setLivre(Livre livre) {
        this.livre = livre;
    }

    /**
     * Obtient l'utilisateur qui a effectué l'emprunt.
     * @return l'utilisateur qui a effectué l'emprunt
     */
    public Utilisateur getUtilisateur() {
        return utilisateur;
    }

    /**
     * Définit l'utilisateur qui a effectué l'emprunt.
     * @param utilisateur l'utilisateur qui a effectué l'emprunt à définir
     */
    public void setUtilisateur(Utilisateur utilisateur) {
        this.utilisateur = utilisateur;
    }
}
