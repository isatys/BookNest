package com.BookNest.BookNestCore.model;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;

/**
 * Représente un livre écrit par un auteur.
 */
@Entity
@Table(name = "livres")
public class Livre {

    /** L'identifiant unique du livre. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le titre du livre. */
    private String titre;

    /** Le genre littéraire du livre. */
    private String genre;

    /** L'auteur qui a écrit le livre. */
    @ManyToOne
    @JsonBackReference // Gère la sérialisation de l'auteur
    private Auteur auteur;

    /**
     * Constructeur par défaut de la classe Livre.
     */
    public Livre() {
    }

    /**
     * Constructeur avec paramètres de la classe Livre.
     * @param id l'identifiant du livre
     */
    public Livre(Long id) {
        this.id = id;
    }

    /**
     * Obtient l'identifiant du livre.
     * @return l'identifiant du livre
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant du livre.
     * @param id l'identifiant du livre à définir
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient le titre du livre.
     * @return le titre du livre
     */
    public String getTitre() {
        return titre;
    }

    /**
     * Définit le titre du livre.
     * @param titre le titre du livre à définir
     */
    public void setTitre(String titre) {
        this.titre = titre;
    }

    /**
     * Obtient le genre littéraire du livre.
     * @return le genre littéraire du livre
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Définit le genre littéraire du livre.
     * @param genre le genre littéraire du livre à définir
     */
    public void setGenre(String genre) {
        this.genre = genre;
    }

    /**
     * Obtient l'auteur du livre.
     * @return l'auteur du livre
     */
    public Auteur getAuteur() {
        return auteur;
    }

    /**
     * Définit l'auteur du livre.
     * @param auteur l'auteur du livre à définir
     */
    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }
}
