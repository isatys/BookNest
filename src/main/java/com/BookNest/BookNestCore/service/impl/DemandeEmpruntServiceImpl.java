package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.Enum.StatutDemande;
import com.BookNest.BookNestCore.dto.DemandeEmpruntDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.mapper.DemandeEmpruntMapper;
import com.BookNest.BookNestCore.mapper.EmpruntMapper;
import com.BookNest.BookNestCore.model.DemandeEmprunt;
import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.repository.DemandeEmpruntRepository;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.repository.UtilisateurRepository;
import com.BookNest.BookNestCore.service.DemandeEmpruntService;
import com.BookNest.BookNestCore.service.LivreService;
import com.BookNest.BookNestCore.service.UtilisateurService;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class DemandeEmpruntServiceImpl implements DemandeEmpruntService {
    @Autowired
    private DemandeEmpruntRepository demandeEmpruntRepository;

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private LivreRepository livreRepository; // Ajoutez ce repository
    @Autowired
    private UtilisateurRepository utilisateurRepository; // Ajoutez ce repository

    @Autowired
    private LivreService livreService;

    @Autowired
    private UtilisateurService utilisateurService;

    @Autowired
    private EmpruntMapper empruntMapper;




    public DemandeEmprunt findById(Long demandeId) {
        return demandeEmpruntRepository.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée."));
    }


    @Override
    public List<DemandeEmpruntDTO> getAllDemandeEmprunts(){
        List<DemandeEmprunt> emprunts = demandeEmpruntRepository.findAll();
        if (emprunts.isEmpty()) {
            throw new EntityNotFoundException("Aucun livre trouvé.");
        }
        return emprunts.stream().map(DemandeEmpruntMapper.INSTANCE::toDto).collect(Collectors.toList());

    }
    @Override
    public void creerDemandeEmprunt(Long livreId, String nom) {
        DemandeEmprunt demande = new DemandeEmprunt();

        // Récupérez les objets Livre et Utilisateur depuis les services ou repositories
        LivreDTO livre = livreService.getLivreById(livreId);
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(nom);

        demande.setLivre(empruntMapper.mapLivreFromId(livre.getId()));
        demande.setUtilisateur(empruntMapper.mapUtilisateurFromId(utilisateur.getId()));
        demande.setDateDemande(LocalDate.now().atStartOfDay()); // Assurez-vous que dateDemande est bien initialisée
        // Set the initial status for the demand
        demande.setStatut(StatutDemande.EN_ATTENTE);

        demandeEmpruntRepository.save(demande);
    }

    public List<DemandeEmprunt> getDemandesEnAttente() {
        return demandeEmpruntRepository.findByStatut(StatutDemande.EN_ATTENTE);
    }

    public void accepterDemande(Long demandeId) {
        DemandeEmprunt demande = demandeEmpruntRepository.findById(demandeId).orElseThrow();
        demande.setStatut(StatutDemande.ACCEPTE);
        demandeEmpruntRepository.save(demande);

        Emprunt emprunt = new Emprunt();
        emprunt.setLivre(demande.getLivre()); // Utilisez les entités
        emprunt.setUtilisateur(demande.getUtilisateur());
        empruntRepository.save(emprunt);
    }

    public void refuserDemande(Long demandeId) {
        DemandeEmprunt demande = demandeEmpruntRepository.findById(demandeId).orElseThrow();
        demande.setStatut(StatutDemande.REFUSE);
        demandeEmpruntRepository.save(demande);
    }
}
