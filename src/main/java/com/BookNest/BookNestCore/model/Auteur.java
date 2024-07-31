package com.BookNest.BookNestCore.model;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import org.mapstruct.Named;

import java.util.List;

/**
 * Représente un auteur d'un livre.
 */
@Entity
@Table(name = "auteur")
public class Auteur {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le nom de l'auteur. */
    @Column(nullable = false)
    private String nom;

    /** La biographie de l'auteur. */
    private String biographie;

    /** Liste des livres écrits par cet auteur. */
    @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
    @JsonIgnore // Prevents recursion during JSON serialization
    private List<Livre> livres;

    /**
     * Obtient l'identifiant de l'auteur.
     * @return l'identifiant de l'auteur
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'auteur.
     * @param id l'identifiant de l'auteur à définir
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient le nom de l'auteur.
     * @return le nom de l'auteur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'auteur.
     * @param nom le nom de l'auteur à définir
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient la biographie de l'auteur.
     * @return la biographie de l'auteur
     */
    public String getBiographie() {
        return biographie;
    }

    /**
     * Définit la biographie de l'auteur.
     * @param biographie la biographie de l'auteur à définir
     */
    public void setBiographie(String biographie) {
        this.biographie = biographie;
    }

    /**
     * Obtient la liste des livres écrits par cet auteur.
     * @return la liste des livres de l'auteur
     */
    public List<Livre> getLivres() {
        return livres;
    }

    /**
     * Définit la liste des livres écrits par cet auteur.
     * @param livres la liste des livres à définir
     */
    public void setLivres(List<Livre> livres) {
        this.livres = livres;
    }
}
