package com.BookNest.BookNestCore.service.impl;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.mapper.LivreMapper;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.repository.AuteurRepository;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.service.impl.LivreServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
}