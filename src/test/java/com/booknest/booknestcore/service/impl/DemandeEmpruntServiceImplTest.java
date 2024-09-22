package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.Enum.StatutDemande;
import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.dto.UtilisateurDTO;
import com.booknest.booknestcore.mapper.DemandeEmpruntMapper;
import com.booknest.booknestcore.mapper.EmpruntMapper;
import com.booknest.booknestcore.model.DemandeEmprunt;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.model.Livre;
import com.booknest.booknestcore.model.Utilisateur;
import com.booknest.booknestcore.repository.DemandeEmpruntRepository;
import com.booknest.booknestcore.repository.EmpruntRepository;
import com.booknest.booknestcore.repository.LivreRepository;
import com.booknest.booknestcore.repository.UtilisateurRepository;
import com.booknest.booknestcore.service.LivreService;
import com.booknest.booknestcore.service.UtilisateurService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


 class DemandeEmpruntServiceImplTest {

    @Mock
    private DemandeEmpruntRepository demandeEmpruntRepository;

    @Mock
    private EmpruntRepository empruntRepository;

    @Mock
    private LivreRepository livreRepository;

    @Mock
    private UtilisateurRepository utilisateurRepository;

    @Mock
    private LivreService livreService;

    @Mock
    private UtilisateurService utilisateurService;

    @Mock
    private DemandeEmpruntMapper demandeEmpruntMapper;

    @Mock
    private EmpruntMapper empruntMapper;

    @InjectMocks
    private DemandeEmpruntServiceImpl demandeEmpruntService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void findById_ShouldReturnDemandeEmprunt() {
        // Arrange
        DemandeEmprunt demande = new DemandeEmprunt();
        when(demandeEmpruntRepository.findById(1L)).thenReturn(Optional.of(demande));

        // Act
        DemandeEmprunt result = demandeEmpruntService.findById(1L);

        // Assert
        assertNotNull(result);
    }

    @Test
    void findById_DemandeNotFound_ShouldThrowException() {
        // Arrange
        when(demandeEmpruntRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThrows(IllegalArgumentException.class, () -> demandeEmpruntService.findById(1L));
    }

    @Test
    void getAllDemandeEmprunts_ShouldReturnListOfDemandeEmpruntDTOs() {
        // Arrange
        DemandeEmprunt demande1 = new DemandeEmprunt();
        DemandeEmprunt demande2 = new DemandeEmprunt();
        DemandeEmpruntDTO dto1 = new DemandeEmpruntDTO();
        dto1.setStatut(StatutDemande.EN_ATTENTE);

        when(demandeEmpruntRepository.findAll()).thenReturn(Arrays.asList(demande1, demande2));

        // Act
        List<DemandeEmpruntDTO> result = demandeEmpruntService.getAllDemandeEmprunts();

        // Assert
        assertEquals(2, result.size());
    }


    @Test
    @Transactional
    void creerDemandeEmprunt_ShouldSaveDemande() {
        // Arrange
        Long livreId = 1L;
        String nom = "John Doe";
        LocalDate dateEmprunt = LocalDate.now();
        LocalDate dateRetour = LocalDate.now().plusDays(10);

        LivreDTO livreDTO = new LivreDTO();
        livreDTO.setId(livreId);
        UtilisateurDTO utilisateurDTO = new UtilisateurDTO();
        utilisateurDTO.setId(1L);

        Livre livre = new Livre();
        livre.setId(livreId);
        Utilisateur utilisateur = new Utilisateur();
        utilisateur.setId(1L);

        when(livreService.getLivreById(livreId)).thenReturn(livreDTO);
        when(utilisateurService.getUtilisateurByNom(nom)).thenReturn(utilisateurDTO);
        when(empruntMapper.mapLivreFromId(livreId)).thenReturn(livre);
        when(empruntMapper.mapUtilisateurFromId(utilisateurDTO.getId())).thenReturn(utilisateur);

        // Act
        demandeEmpruntService.creerDemandeEmprunt(livreId, nom, dateEmprunt, dateRetour);

        // Assert
        verify(demandeEmpruntRepository, times(1)).save(any(DemandeEmprunt.class));
    }

    @Test
    void getDemandesEnAttente_ShouldReturnListOfDemandeEmprunts() {
        // Arrange
        DemandeEmprunt demande = new DemandeEmprunt();
        when(demandeEmpruntRepository.findByStatut(StatutDemande.EN_ATTENTE)).thenReturn(Collections.singletonList(demande));

        // Act
        List<DemandeEmprunt> result = demandeEmpruntService.getDemandesEnAttente();

        // Assert
        assertEquals(1, result.size());
    }

    @Test
    @Transactional
    void accepterDemande_ShouldUpdateStatutAndSaveEmprunt() {
        // Arrange
        Long demandeId = 1L;
        DemandeEmprunt demande = new DemandeEmprunt();
        demande.setStatut(StatutDemande.EN_ATTENTE);
        when(demandeEmpruntRepository.findById(demandeId)).thenReturn(Optional.of(demande));

        // Act
        demandeEmpruntService.accepterDemande(demandeId);

        // Assert
        assertEquals(StatutDemande.ACCEPTE, demande.getStatut());
        verify(demandeEmpruntRepository, times(1)).save(demande);
        verify(empruntRepository, times(1)).save(any(Emprunt.class));
    }

    @Test
    @Transactional
    void refuserDemande_ShouldUpdateStatut() {
        // Arrange
        Long demandeId = 1L;
        DemandeEmprunt demande = new DemandeEmprunt();
        demande.setStatut(StatutDemande.EN_ATTENTE);
        when(demandeEmpruntRepository.findById(demandeId)).thenReturn(Optional.of(demande));

        // Act
        demandeEmpruntService.refuserDemande(demandeId);

        // Assert
        assertEquals(StatutDemande.REFUSE, demande.getStatut());
        verify(demandeEmpruntRepository, times(1)).save(demande);
    }
}
