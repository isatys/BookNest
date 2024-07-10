package com.BookNest.BookNestCore.model;

import jakarta.persistence.*;
import java.util.List;

/**
 * Représente un utilisateur de la bibliothèque.
 */
@Entity
@Table(name = "utilisateur")
public class Utilisateur {

    /** L'identifiant unique de l'utilisateur. */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    /** Le nom de l'utilisateur. */
    private String nom;

    /** L'adresse de l'utilisateur. */
    private String adresse;

    /** La liste des emprunts effectués par l'utilisateur. */
    @OneToMany(mappedBy = "utilisateur", cascade = CascadeType.ALL)
    private List<Emprunt> emprunts;

    /**
     * Obtient l'identifiant de l'utilisateur.
     * @return l'identifiant de l'utilisateur
     */
    public Long getId() {
        return id;
    }

    /**
     * Définit l'identifiant de l'utilisateur.
     * @param id l'identifiant de l'utilisateur à définir
     */
    public void setId(Long id) {
        this.id = id;
    }

    /**
     * Obtient le nom de l'utilisateur.
     * @return le nom de l'utilisateur
     */
    public String getNom() {
        return nom;
    }

    /**
     * Définit le nom de l'utilisateur.
     * @param nom le nom de l'utilisateur à définir
     */
    public void setNom(String nom) {
        this.nom = nom;
    }

    /**
     * Obtient l'adresse de l'utilisateur.
     * @return l'adresse de l'utilisateur
     */
    public String getAdresse() {
        return adresse;
    }

    /**
     * Définit l'adresse de l'utilisateur.
     * @param adresse l'adresse de l'utilisateur à définir
     */
    public void setAdresse(String adresse) {
        this.adresse = adresse;
    }

    /**
     * Obtient la liste des emprunts effectués par l'utilisateur.
     * @return la liste des emprunts de l'utilisateur
     */
    public List<Emprunt> getEmprunts() {
        return emprunts;
    }

    /**
     * Définit la liste des emprunts effectués par l'utilisateur.
     * @param emprunts la liste des emprunts de l'utilisateur à définir
     */
    public void setEmprunts(List<Emprunt> emprunts) {
        this.emprunts = emprunts;
    }



}
