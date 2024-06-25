    package com.BookNest.model;

    import javax.persistence.Entity;
    import javax.persistence.Id;
    import javax.persistence.OneToMany;
    import java.util.List;

    @Entity
    public class Auteur {

        @Id
        private Long id;
        private String nom;
        private String biographie;

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

        @OneToMany(mappedBy = "auteur")
        private List<Livre> livres;

    }
