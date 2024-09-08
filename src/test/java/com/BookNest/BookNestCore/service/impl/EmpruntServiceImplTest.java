package com.BookNest.BookNestCore.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.mapper.EmpruntMapper;
import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.Utilisateur;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.repository.UserRepository;
import com.BookNest.BookNestCore.service.impl.EmpruntServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.MockitoAnnotations;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

class EmpruntServiceImplTest {

    private EmpruntRepository empruntRepository;
    private EmpruntMapper empruntMapper;
    private LivreRepository livreRepository;
    private UserRepository userRepository;
    private EmpruntServiceImpl empruntService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        empruntRepository = mock(EmpruntRepository.class);
        empruntMapper = mock(EmpruntMapper.class);
        livreRepository = mock(LivreRepository.class);
        userRepository = mock(UserRepository.class);
        empruntService = new EmpruntServiceImpl();
        empruntService.empruntRepository = empruntRepository;
        empruntService.empruntMapper = empruntMapper;
        empruntService.livreRepository = livreRepository;
        empruntService.userRepository = userRepository;
    }

    @Test
    void getEmprunts_ShouldReturnListOfEmprunts() {
        // Arrange
        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        when(empruntRepository.findAll()).thenReturn(Arrays.asList(emprunt1, emprunt2));

        // Act
        List<Emprunt> result = empruntService.getEmprunts();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(emprunt1));
        assertTrue(result.contains(emprunt2));
    }

    @Test
    void retournerLivre_ShouldDeleteEmprunt() {
        // Arrange
        Long empruntId = 1L;
        Emprunt emprunt = new Emprunt();
        when(empruntRepository.findById(empruntId)).thenReturn(Optional.of(emprunt));

        // Act
        empruntService.retournerLivre(empruntId);

        // Assert
        verify(empruntRepository, times(1)).delete(emprunt);
    }

    @Test
    void retournerLivre_ShouldThrowExceptionWhenEmpruntNotFound() {
        // Arrange
        Long empruntId = 1L;
        when(empruntRepository.findById(empruntId)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> empruntService.retournerLivre(empruntId));
    }

    @Test
    void getAllEmprunts_ShouldReturnListOfEmpruntDTOs() {
        // Arrange
        Emprunt emprunt1 = new Emprunt();
        Emprunt emprunt2 = new Emprunt();
        EmpruntDTO dto1 = new EmpruntDTO();
        EmpruntDTO dto2 = new EmpruntDTO();

        when(empruntRepository.findAll()).thenReturn(Arrays.asList(emprunt1, emprunt2));
        when(empruntMapper.toDto(emprunt1)).thenReturn(dto1);
        when(empruntMapper.toDto(emprunt2)).thenReturn(dto2);

        // Act
        List<EmpruntDTO> result = empruntService.getAllEmprunts();

        // Assert
        assertEquals(2, result.size());
        assertTrue(result.contains(dto1));
        assertTrue(result.contains(dto2));
    }

    @Test
    void getEmpruntById_ShouldReturnEmpruntDTO() {
        // Arrange
        Long id = 1L;
        Emprunt emprunt = new Emprunt();
        EmpruntDTO dto = new EmpruntDTO();

        when(empruntRepository.findById(id)).thenReturn(Optional.of(emprunt));
        when(empruntMapper.toDto(emprunt)).thenReturn(dto);

        // Act
        EmpruntDTO result = empruntService.getEmpruntById(id);

        // Assert
        assertEquals(dto, result);
    }

    @Test
    void createEmprunt_ShouldSaveEmprunt() {
        // Arrange
        EmpruntDTO empruntDTO = new EmpruntDTO();
        Emprunt emprunt = new Emprunt();

        when(empruntMapper.toEntity(empruntDTO)).thenReturn(emprunt);

        // Act
        empruntService.createEmprunt(empruntDTO);

        // Assert
        verify(empruntRepository, times(1)).save(emprunt);
    }

    @Test
    void updateEmprunt_ShouldUpdateAndSaveEmprunt() {
        // Arrange
        Long id = 1L;

        // Créer un DTO avec des données d'exemple
        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setId(1L); // Définir un ID factice

        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(2L); // Définir un ID factice

        EmpruntDTO empruntDTO = new EmpruntDTO();
        empruntDTO.setLivre(livreDTO); // Initialiser les propriétés du DTO
        empruntDTO.setUtilisateur(utilisateurDTO);
        empruntDTO.setDateEmprunt(LocalDate.now());
        empruntDTO.setDateRetour(LocalDate.now().plusDays(7));

        Emprunt emprunt = new Emprunt();

        when(empruntRepository.findById(id)).thenReturn(Optional.of(emprunt));
        when(empruntMapper.mapLivreFromId(livreDTO.getId())).thenReturn(new Livre());
        when(empruntMapper.mapUtilisateurFromId(utilisateurDTO.getId())).thenReturn(new Utilisateur());

        // Act
        empruntService.updateEmprunt(id, empruntDTO);

        // Assert
        verify(empruntRepository, times(1)).save(emprunt);
        assertEquals(empruntDTO.getDateEmprunt(), emprunt.getDateEmprunt());
        assertEquals(empruntDTO.getDateRetour(), emprunt.getDateRetour());
        // Ajoutez d'autres assertions selon ce qui doit être vérifié
    }


    @Test
    void deleteEmprunt_ShouldDeleteEmprunt() {
        // Arrange
        Long id = 1L;

        // Act
        empruntService.deleteEmprunt(id);

        // Assert
        verify(empruntRepository, times(1)).deleteById(id);
    }

    @Test
    void getAvailableBooks_ShouldReturnListOfAvailableBooks() {
        // Arrange
        Livre livre1 = new Livre();
        Livre livre2 = new Livre();
        livre1.setId(1L);
        livre2.setId(2L);

        Emprunt emprunt = new Emprunt();
        emprunt.setLivre(livre1);

        when(empruntRepository.findAll()).thenReturn(Arrays.asList(emprunt));
        when(livreRepository.findAll()).thenReturn(Arrays.asList(livre1, livre2));

        // Act
        List<Livre> result = empruntService.getAvailableBooks();

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(livre2));
    }

    @Test
    void getEmpruntsByUtilisateur_ShouldReturnListOfEmpruntDTOs() {
        // Arrange
        Long utilisateurId = 1L;
        Emprunt emprunt = new Emprunt();
        EmpruntDTO dto = new EmpruntDTO();

        when(empruntRepository.findByUtilisateurId(utilisateurId)).thenReturn(Arrays.asList(emprunt));
        when(empruntMapper.toDto(emprunt)).thenReturn(dto);

        // Act
        List<EmpruntDTO> result = empruntService.getEmpruntsByUtilisateur(utilisateurId);

        // Assert
        assertEquals(1, result.size());
        assertTrue(result.contains(dto));
    }

    @Test
    void findById_ShouldReturnEmprunt() {
        // Arrange
        Long id = 1L;
        Emprunt emprunt = new Emprunt();

        when(empruntRepository.findById(id)).thenReturn(Optional.of(emprunt));

        // Act
        Emprunt result = empruntService.findById(id);

        // Assert
        assertEquals(emprunt, result);
    }
}

