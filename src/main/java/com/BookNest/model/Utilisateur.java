package com.BookNest.model;


import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import java.util.List;

@Entity
public class Utilisateur {

    @Id
    private Long id;
    private String nom;
    private String adresse;

    @OneToMany(mappedBy = "utilisation")
    private List<Emprunt> emprunts;

}

