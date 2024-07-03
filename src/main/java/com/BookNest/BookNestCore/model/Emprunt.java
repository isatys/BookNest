package com.BookNest.BookNestCore.model;

import jakarta.persistence.*;

import java.util.Date;

@Entity
public class Emprunt {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private Date dateEmprunt;
    private Date dateRetour;

    @ManyToOne
    private Livre livre;

    @ManyToOne
    private Utilisateur utilisateur;


}
