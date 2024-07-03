package com.BookNest.BookNestCore.model;


import jakarta.persistence.*;

@Entity
@Table(name = "livres")
public class Livre {
    public Livre() {
    }

    public Livre(Long id, String titre, String genre, Auteur auteur) {
        this.id = id;
        this.titre = titre;
        this.genre = genre;
        this.auteur = auteur;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitre() {
        return titre;
    }

    public void setTitre(String titre) {
        this.titre = titre;
    }

    public String getGenre() {
        return genre;
    }

    public void setGenre(String genre) {
        this.genre = genre;
    }

    public Auteur getAuteur() {
        return auteur;
    }

    public void setAuteur(Auteur auteur) {
        this.auteur = auteur;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String titre;
    private String genre;

    @ManyToOne
    private Auteur auteur;




}
