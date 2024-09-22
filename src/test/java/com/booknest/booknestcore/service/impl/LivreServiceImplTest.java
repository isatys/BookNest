package com.booknest.booknestcore.service.impl;
import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.mapper.LivreMapper;
import com.booknest.booknestcore.model.Auteur;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.model.Livre;
import com.booknest.booknestcore.repository.LivreRepository;
import com.booknest.booknestcore.repository.AuteurRepository;
import com.booknest.booknestcore.repository.EmpruntRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class LivreServiceImplTest {

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private AuteurRepository auteurRepository;

    @Mock
    private EmpruntRepository empruntRepository;

    @InjectMocks
    private LivreServiceImpl livreService;

    @Mock
    private LivreMapper livreMapper;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void updateLivre_ShouldUpdateAndSaveLivre() {
        // Arrange
        Long id = 1L;
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitre("New Title");
        livreDTO.setGenre("New Genre");
        livreDTO.setNomAuteur("Author Name");

        Livre existingLivre = new Livre();
        existingLivre.setId(id); // Assurez-vous que l'ID correspond
        existingLivre.setTitre("Old Title");
        existingLivre.setGenre("Old Genre");

        Livre updatedLivre = new Livre();
        updatedLivre.setId(id); // Assurez-vous que l'ID correspond
        updatedLivre.setTitre("New Title");
        updatedLivre.setGenre("New Genre");

        // Configuration des mocks
        when(livreRepository.findById(id)).thenReturn(Optional.of(existingLivre));
        when(livreRepository.save(existingLivre)).thenReturn(updatedLivre);

        // Configuration du mock du mapper
        when(livreMapper.livreDTOToLivre(livreDTO)).thenReturn(existingLivre);
        when(livreMapper.livreToLivreDTO(updatedLivre)).thenReturn(livreDTO);

        // Act
        LivreDTO result = livreService.updateLivre(id, livreDTO);

        // Assert
        assertNotNull(result, "Le résultat ne doit pas être null");
        assertEquals("New Title", result.getTitre());
        assertEquals("New Genre", result.getGenre());

        // Vérifiez que la méthode save() du repository a été appelée avec l'entité mise à jour
        verify(livreRepository).save(existingLivre); // Vous pouvez aussi spécifier les arguments exacts si nécessaire
    }
    // Test pour la méthode getLivreById
    @Test
    void getLivreById_ShouldReturnLivreDTO_WhenLivreExists() {
        // Arrange
        Long id = 1L;
        Livre livre = new Livre();
        livre.setId(id);
        livre.setTitre("Book Title");

        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitre("Book Title");

        when(livreRepository.findById(id)).thenReturn(Optional.of(livre));
        when(livreMapper.livreToLivreDTO(livre)).thenReturn(livreDTO);

        // Act
        LivreDTO result = livreService.getLivreById(id);

        // Assert
        assertNotNull(result);
        assertEquals("Book Title", result.getTitre());
    }

    @Test
    void getLivreById_ShouldReturnNull_WhenLivreDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(livreRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        LivreDTO result = livreService.getLivreById(id);

        // Assert
        assertNull(result);
    }

    // Test pour la méthode createLivre
    @Test
    void createLivre_ShouldSaveAndReturnLivreDTO_WhenAuteurExists() {
        // Arrange
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setNomAuteur("Auteur Test");
        livreDTO.setTitre("Titre Livre");

        Auteur auteur = new Auteur();
        auteur.setNom("Auteur Test");

        Livre livre = new Livre();
        livre.setTitre("Titre Livre");
        livre.setAuteurNom("Auteur Test");

        when(auteurRepository.findByNom(any())).thenReturn(auteur);
        when(livreMapper.livreDTOToLivre(any())).thenReturn(livre);
        when(livreRepository.save(any())).thenReturn(livre);
        when(livreMapper.livreToLivreDTO(any())).thenReturn(livreDTO);

        // Act
        LivreDTO result = livreService.createLivre(livreDTO);

        // Assert
        assertNotNull(result);
        assertEquals("Titre Livre", result.getTitre());

        // Utiliser any() pour ignorer la comparaison de la référence exacte
        verify(livreRepository).save(any(Livre.class));
    }


    @Test
    void createLivre_ShouldThrowException_WhenAuteurNotSpecified() {
        // Arrange
        LivreDTO livreDTO = new LivreDTO();

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            livreService.createLivre(livreDTO);
        });

        assertEquals("L'auteur doit être spécifié.", exception.getMessage());
    }

    // Test pour la méthode deleteLivre
    @Test
    void deleteLivre_ShouldDeleteLivre_WhenLivreExists() {
        // Arrange
        Long id = 1L;
        when(livreRepository.existsById(id)).thenReturn(true);

        // Act
        String result = livreService.deleteLivre(id);

        // Assert
        assertEquals("Livre avec l'ID " + id + " a été supprimé avec succès.", result);
        verify(livreRepository).deleteById(id);
    }

    @Test
    void deleteLivre_ShouldThrowException_WhenLivreDoesNotExist() {
        // Arrange
        Long id = 1L;
        when(livreRepository.existsById(id)).thenReturn(false);

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            livreService.deleteLivre(id);
        });

        assertEquals("Livre avec l'ID " + id + " non trouvé.", exception.getMessage());
    }

    // Test pour la méthode getAllLivres
    @Test
    void getAllLivres_ShouldReturnLivreDTOList_WhenLivresExist() {
        // Arrange
        List<Livre> livres = Arrays.asList(new Livre(), new Livre());
        List<LivreDTO> livreDTOs = Arrays.asList(new LivreDTO(), new LivreDTO());

        when(livreRepository.findAll()).thenReturn(livres);
        when(livreMapper.livreToLivreDTO(any(Livre.class))).thenReturn(new LivreDTO());

        // Act
        List<LivreDTO> result = livreService.getAllLivres();

        // Assert
        assertEquals(2, result.size());
    }

    @Test
    void getAllLivres_ShouldThrowException_WhenNoLivresFound() {
        // Arrange
        when(livreRepository.findAll()).thenReturn(List.of());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            livreService.getAllLivres();
        });

        assertEquals("Aucun livre trouvé.", exception.getMessage());
    }

    // Test pour la méthode getAllLivresPage
    @Test
    void getAllLivresPage_ShouldReturnPaginatedLivreDTOs_WhenLivresExist() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        Page<Livre> livresPage = new PageImpl<>(Arrays.asList(new Livre(), new Livre()));
        when(livreRepository.findAll(pageable)).thenReturn(livresPage);
        when(livreMapper.livreToLivreDTO(any(Livre.class))).thenReturn(new LivreDTO());

        // Act
        Page<LivreDTO> result = livreService.getAllLivresPage(pageable);

        // Assert
        assertEquals(2, result.getTotalElements());
    }

    @Test
    void getAllLivresPage_ShouldThrowException_WhenNoLivresFound() {
        // Arrange
        Pageable pageable = PageRequest.of(0, 2);
        when(livreRepository.findAll(pageable)).thenReturn(Page.empty());

        // Act & Assert
        EntityNotFoundException exception = assertThrows(EntityNotFoundException.class, () -> {
            livreService.getAllLivresPage(pageable);
        });

        assertEquals("Aucun livre trouvé.", exception.getMessage());
    }

    @Test
    void getBestSellers_ShouldReturnListOfBestSellers() {
        // Act
        List<Livre> bestSellers = livreService.getBestSellers();

        // Assert
        assertNotNull(bestSellers);
        assertEquals(1, bestSellers.size());
        assertEquals("The Passion Within", bestSellers.get(0).getTitre());
    }
    @Test
    void getNewArrivals_ShouldReturnListOfNewArrivals() {
        // Act
        List<Livre> newArrivals = livreService.getNewArrivals();

        // Assert
        assertNotNull(newArrivals);
        assertEquals(1, newArrivals.size());
        assertEquals("Your Soul is the river", newArrivals.get(0).getTitre());
    }
    @Test
    void getMostBorrowedBooks_ShouldReturnListOfMostBorrowedBooks() {
        // Act
        List<Livre> mostBorrowedBooks = livreService.getMostBorrowedBooks();

        // Assert
        assertNotNull(mostBorrowedBooks);
        assertEquals(1, mostBorrowedBooks.size());
        assertEquals("Your heart is the sea", mostBorrowedBooks.get(0).getTitre());
    }
    @Test
    void getRecommendedBooksForUser_ShouldReturnListOfRecommendedBooks() {
        // Act
        List<Livre> recommendedBooks = livreService.getRecommendedBooksForUser();

        // Assert
        assertNotNull(recommendedBooks);
        assertEquals(1, recommendedBooks.size());
        assertEquals("My secret plan to rule the world", recommendedBooks.get(0).getTitre());
    }
    @Test
    void getLivresByGenre_ShouldReturnPagedLivresByGenre() {
        // Arrange
        String genre = "Romantique";
        Livre livre = new Livre();
        livre.setGenre(genre);
        Page<Livre> livresPage = new PageImpl<>(List.of(livre), PageRequest.of(0, 1), 1);

        when(livreRepository.findByGenre(eq(genre), any(Pageable.class)))
                .thenReturn(livresPage);

        // Act
        Page<LivreDTO> result = livreService.getLivresByGenre(genre, PageRequest.of(0, 1));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals(genre, result.getContent().get(0).getGenre());
    }
    @Test
    void searchLivres_ShouldReturnPagedLivresBySearch() {
        // Arrange
        String search = "Titre";
        Livre livre = new Livre();
        livre.setTitre("Titre Livre");
        Page<Livre> livresPage = new PageImpl<>(List.of(livre), PageRequest.of(0, 1), 1);

        when(livreRepository.findByTitreContainingIgnoreCase(eq(search), any(Pageable.class)))
                .thenReturn(livresPage);

        // Act
        Page<LivreDTO> result = livreService.searchLivres(search, PageRequest.of(0, 1));

        // Assert
        assertNotNull(result);
        assertEquals(1, result.getTotalElements());
        assertEquals("Titre Livre", result.getContent().get(0).getTitre());
    }
    @Test
    void isLivreDisponible_ShouldReturnTrue_WhenNoActiveEmpruntExists() {
        // Arrange
        Long livreId = 1L;
        when(empruntRepository.findByLivreIdAndDateRetourIsNull(livreId)).thenReturn(Collections.emptyList());

        // Act
        boolean isDisponible = livreService.isLivreDisponible(livreId);

        // Assert
        assertTrue(isDisponible);
    }

    @Test
    void isLivreDisponible_ShouldReturnFalse_WhenActiveEmpruntExists() {
        // Arrange
        Long livreId = 1L;
        Emprunt emprunt = new Emprunt();
        when(empruntRepository.findByLivreIdAndDateRetourIsNull(livreId)).thenReturn(List.of(emprunt));

        // Act
        boolean isDisponible = livreService.isLivreDisponible(livreId);

        // Assert
        assertFalse(isDisponible);
    }

    @Test
    void getDistinctGenres_ShouldReturnListOfDistinctGenres() {
        // Arrange
        List<String> genres = List.of("Romantique", "Comédie", "Horreur");
        when(livreRepository.findDistinctGenres()).thenReturn(genres);

        // Act
        List<String> result = livreService.getDistinctGenres();

        // Assert
        assertNotNull(result); // Vérifie que le résultat n'est pas null
        assertEquals(3, result.size()); // Vérifie la taille de la liste
        assertTrue(result.contains("Romantique")); // Vérifie que la liste contient "Romantique"
        assertTrue(result.contains("Comédie")); // Vérifie que la liste contient "Comédie"
        assertTrue(result.contains("Horreur")); // Vérifie que la liste contient "Horreur"

        // Vérifie que la méthode du repository a bien été appelée
        verify(livreRepository, times(1)).findDistinctGenres();
    }
    @Test
    void updateLivre_ShouldReturnNull_WhenLivreDoesNotExist() {
        // Arrange
        Long id = 1L;
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setTitre("Titre Inexistant");
        livreDTO.setGenre("Genre Inexistant");

        // Simuler le fait que le livre n'existe pas dans le repository
        when(livreRepository.findById(id)).thenReturn(Optional.empty());

        // Act
        LivreDTO result = livreService.updateLivre(id, livreDTO);

        // Assert
        assertNull(result); // Vérifie que le résultat est bien null

        // Vérifiez que la méthode save() n'a pas été appelée
        verify(livreRepository, never()).save(any(Livre.class));
    }
    @Test
    void getAllAuteurs_ShouldReturnListOfAuteurs() {
        // Arrange
        Auteur auteur1 = new Auteur();
        auteur1.setId(1L);
        auteur1.setBiographie("bio");
        auteur1.setNom("Auteur 1");

        Auteur auteur2 = new Auteur();
        auteur2.setId(2L);
        auteur2.setBiographie("bio");
        auteur2.setNom("Auteur 2");
        List<Auteur> auteurs = List.of(auteur1,auteur2);

        // Simuler le comportement du repository
        when(auteurRepository.findAll()).thenReturn(auteurs);

        // Act
        List<Auteur> result = livreService.getAllAuteurs();

        // Assert
        assertNotNull(result); // Vérifie que le résultat n'est pas null
        assertEquals(2, result.size()); // Vérifie qu'il y a bien 2 auteurs dans la liste
        assertEquals("Auteur 1", result.get(0).getNom()); // Vérifie le nom du premier auteur
        assertEquals("Auteur 2", result.get(1).getNom()); // Vérifie le nom du deuxième auteur

        // Vérifie que la méthode findAll() du repository a été appelée
        verify(auteurRepository).findAll();
    }

}