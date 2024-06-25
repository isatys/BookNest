package com.BookNest.model;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import java.util.Date;

@Entity
public class Emprunt {

    @Id
    private Long id;
    private Date dateEmprunt;
    private Date dateRetour;

    @ManyToOne
    private Livre livre;

    @ManyToOne
    private Utilisateur utilisateur;


}
