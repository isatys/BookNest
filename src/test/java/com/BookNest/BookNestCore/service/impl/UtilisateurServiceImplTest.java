package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.mapper.UtilisateurMapper;
import com.BookNest.BookNestCore.model.Utilisateur;
import com.BookNest.BookNestCore.repository.UtilisateurRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.mockito.Mockito.when;

@SpringBootTest
public class UtilisateurServiceImplTest {

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private UtilisateurMapper utilisateurMapper;

    @InjectMocks
    private UtilisateurServiceImpl utilisateurService;

    public UtilisateurServiceImplTest() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void getUtilisateurByNom_ShouldReturnUtilisateurDTO() {
        // Arrange
        String nom = "User1";
        Utilisateur user = new Utilisateur();
        user.setNom(nom);

        UtilisateurDTO userDTO = new UtilisateurDTO();
        userDTO.setNom(nom);

        when(utilisateurRepository.findByNom(nom)).thenReturn(Optional.of(user));
        when(utilisateurMapper.userToUtilisateurDTO(user)).thenReturn(userDTO);

        // Act
        UtilisateurDTO result = utilisateurService.getUtilisateurByNom(nom);

        // Assert
        assertEquals(nom, result.getNom());
    }

    @Test
    void getUtilisateurByNom_ShouldReturnNullIfNotFound() {
        // Arrange
        String nom = "NonExistingUser";

        when(utilisateurRepository.findByNom(nom)).thenReturn(Optional.empty());

        // Act
        UtilisateurDTO result = utilisateurService.getUtilisateurByNom(nom);

        // Assert
        assertNull(result);
    }

    @Test
    void getAllUtilisateurs_ShouldReturnListOfUtilisateurDTOs() {
        // Arrange
        Utilisateur user1 = new Utilisateur();
        user1.setNom("User1");

        Utilisateur user2 = new Utilisateur();
        user2.setNom("User2");

        UtilisateurDTO userDTO1 = new UtilisateurDTO();
        userDTO1.setNom("User1");

        UtilisateurDTO userDTO2 = new UtilisateurDTO();
        userDTO2.setNom("User2");

        // Simuler l'appel à findAll() pour renvoyer une liste d'utilisateurs
        when(utilisateurRepository.findAll()).thenReturn(Arrays.asList(user1, user2));

        // Simuler le mapping de chaque Utilisateur vers UtilisateurDTO
        when(utilisateurMapper.userToUtilisateurDTO(user1)).thenReturn(userDTO1);
        when(utilisateurMapper.userToUtilisateurDTO(user2)).thenReturn(userDTO2);

        // Act
        List<UtilisateurDTO> result = utilisateurService.getAllUtilisateurs();

        // Assert
        assertEquals(2, result.size());
        assertEquals("User1", result.get(0).getNom());
        assertEquals("User2", result.get(1).getNom());
    }
}
