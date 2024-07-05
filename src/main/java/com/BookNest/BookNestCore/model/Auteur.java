    package com.BookNest.BookNestCore.model;

    import jakarta.persistence.*;
    import org.mapstruct.Named;

    import java.util.List;

    @Entity
    public class Auteur {

        @Id
        @GeneratedValue(strategy = GenerationType.IDENTITY)
        private Long id;
        private String nom;
        private String biographie;
        @OneToMany(mappedBy = "auteur", cascade = CascadeType.ALL)
        private List<Livre> livres;
        public Long getId() {
            return id;
        }

        public void setId(Long id) {
            this.id = id;
        }

        public String getNom() {
            return nom;
        }

        public void setNom(String nom) {
            this.nom = nom;
        }

        public String getBiographie() {
            return biographie;
        }

        public void setBiographie(String biographie) {
            this.biographie = biographie;
        }

        public List<Livre> getLivres() {
            return livres;
        }

        public void setLivres(List<Livre> livres) {
            this.livres = livres;
        }



    }
