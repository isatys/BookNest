# BookNest
BookNest est une application de gestion de bibliothèque numérique conçue pour 
moderniser la façon dont les bibliothèques gèrent leurs collections et interagissent avec leurs utilisateurs.
Elle vise à faciliter l'accès aux livres,
promouvoir la lecture et offrir une expérience utilisateur intuitive et enrichissante.


# Pourquoi utiliser BookNest
* Facilité d'Accès : Accédez à votre bibliothèque et gérez vos emprunts où que vous soyez.
* Sécurité et Confidentialité : Vos données et emprunts sont sécurisés et protégés.
* Promotion de la Lecture : Recevez des recommandations personnalisées et découvrez de nouveaux auteurs et livres.


# Installation et prérequis
* Java 11 ou version ultérieure
* PostgreSQL
* Git

# Étapes d'Installation
  ## Cloner le dépôt GitHub

  ```bash
  git clone https://github.com/votre-utilisateur/BookNest.git
  ```

  ## Accéder au répertoire du projet
  
  ```bash
  cd BookNest
  ```

# Configuration

Pour configurer les paramètres de la base de données, modifiez le fichier `application.properties` :

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/booknest
spring.datasource.username=utilisateur
spring.datasource.password=mot_de_passe
```

# Démarrer l'Application

Pour démarrer l'application, exécutez la commande suivante :

 ```bash
./mvnw spring-boot:run
```
