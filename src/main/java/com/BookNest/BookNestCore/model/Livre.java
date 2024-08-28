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
    @Column(name = "titre")
    private String titre;

    /** Le genre littéraire du livre. */
    @Column(name = "genre")
    private String genre;

    /** Le nom de l'auteur du livre. */
    @Column(name = "auteur_nom")
    private String auteurNom;

    /** URL de l'image de couverture du livre. */
    @Column(name = "image_url")
    private String coverUrl;

    /** L'auteur qui a écrit le livre. */
    @ManyToOne
    @JoinColumn(name = "auteur_id") // Assure que la colonne auteur_id existe dans la table livres
    @JsonBackReference
    private Auteur auteur;

    /**
     * Constructeur par défaut de la classe Livre.
     */
    public Livre() {
    }

    public Livre(String titre, String genre, String auteurNom, String coverUrl) {
        this.titre = titre;
        this.genre = genre;
        this.auteurNom = auteurNom;
        this.coverUrl = coverUrl;
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
     * Obtient le nom de l'auteur du livre.
     * @return le nom de l'auteur du livre
     */
    public String getAuteurNom() {
        return auteurNom;
    }

    /**
     * Définit le nom de l'auteur du livre.
     * @param auteurNom le nom de l'auteur du livre à définir
     */
    public void setAuteurNom(String auteurNom) {
        this.auteurNom = auteurNom;
    }

    /**
     * Obtient l'URL de l'image de couverture du livre.
     * @return l'URL de l'image de couverture du livre
     */
    public String getCoverUrl() {
        return coverUrl;
    }

    /**
     * Définit l'URL de l'image de couverture du livre.
     * @param coverUrl l'URL de l'image de couverture du livre à définir
     */
    public void setCoverUrl(String coverUrl) {
        this.coverUrl = coverUrl;
    }
}
