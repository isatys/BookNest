package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.dto.AuteurDTO;
import com.booknest.booknestcore.model.Auteur;
import com.booknest.booknestcore.repository.AuteurRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.transaction.annotation.Transactional;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


class AuteurServiceImplTest {

    @Mock
    private AuteurRepository auteurRepository;

    @InjectMocks
    private AuteurServiceImpl auteurService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getAllAuthors_ShouldReturnSortedList() {
        // Arrange
        Auteur auteur1 = new Auteur();
        auteur1.setNom("Albert");
        Auteur auteur2 = new Auteur();
        auteur2.setNom("Zora");

        when(auteurRepository.findAll()).thenReturn(Arrays.asList(auteur1, auteur2));

        // Act
        List<AuteurDTO> result = auteurService.getAllAuthors();

        // Assert
        assertEquals(2, result.size());
        assertEquals("Albert", result.get(0).getNom());
        assertEquals("Zora", result.get(1).getNom());
    }

    @Test
    void getAuthorById_AuthorExists_ShouldReturnAuthorDTO() {
        // Arrange
        Auteur auteur = new Auteur();
        auteur.setNom("Albert");
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        // Act
        AuteurDTO result = auteurService.getAuthorById(1L);

        // Assert
        assertNotNull(result);
        assertEquals("Albert", result.getNom());
    }

    @Test
    void getAuthorById_AuthorNotFound_ShouldReturnNull() {
        // Arrange
        when(auteurRepository.findById(1L)).thenReturn(Optional.empty());

        // Act
        AuteurDTO result = auteurService.getAuthorById(1L);

        // Assert
        assertNull(result);
    }

    @Test
    @Transactional
    void createAuthor_ShouldSaveAuthor() {
        // Arrange
        AuteurDTO auteurDTO = new AuteurDTO();
        auteurDTO.setNom("Albert");
        auteurDTO.setBiographie("Biographie");
        Auteur auteur = new Auteur();
        auteur.setNom("Albert");
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        // Act
        auteurService.updateAuteur(1L, auteurDTO);
        auteurService.createAuthor(auteurDTO);

        // Assert
        verify(auteurRepository, times(1)).save(auteur);
    }

    @Test
    @Transactional
    void deleteAuthor_ShouldRemoveAuthor() {
        // Act
        auteurService.deleteAuthor(1L);

        // Assert
        verify(auteurRepository, times(1)).deleteById(1L);
    }

    @Test
    @Transactional
    void updateAuteur_AuthorExists_ShouldUpdateFields() {
        // Arrange
        AuteurDTO auteurDTO = new AuteurDTO();
        auteurDTO.setBiographie("nouvelle bio");
        auteurDTO.setNom( "teo");
        Auteur auteur = new Auteur();
        auteur.setNom("Old Name");
        auteur.setBiographie("Old Bio");
        when(auteurRepository.findById(1L)).thenReturn(Optional.of(auteur));

        // Act
        auteurService.updateAuteur(1L, auteurDTO);

        // Assert
        assertEquals("teo", auteur.getNom());
        assertEquals("nouvelle bio", auteur.getBiographie());
        verify(auteurRepository, times(1)).save(auteur);
    }

    @Test
    @Transactional
    void updateAuteur_AuthorNotFound_ShouldThrowException() {
        // Arrange
        AuteurDTO auteurDTO = new AuteurDTO();
        auteurDTO.setNom( "Updated Name");
        auteurDTO.setBiographie("Updated Bio");
        when(auteurRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(RuntimeException.class, () -> auteurService.updateAuteur(1L, auteurDTO));
    }

    @Test
    void searchAuthorsByName_ShouldReturnMatchingAuthors() {
        // Arrange
        Auteur auteur = new Auteur();
        auteur.setBiographie("Biographie");
        auteur.setNom("Albert");
        when(auteurRepository.findByNomContainingIgnoreCase("Albert")).thenReturn(Collections.singletonList(auteur));

        // Act
        List<AuteurDTO> result = auteurService.searchAuthorsByName("Albert");

        // Assert
        assertEquals(1, result.size());
        assertEquals("Albert", result.get(0).getNom());
    }
}
