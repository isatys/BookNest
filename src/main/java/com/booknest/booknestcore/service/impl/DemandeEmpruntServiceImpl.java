package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.Enum.StatutDemande;
import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.dto.UtilisateurDTO;
import com.booknest.booknestcore.mapper.DemandeEmpruntMapper;
import com.booknest.booknestcore.mapper.EmpruntMapper;
import com.booknest.booknestcore.model.DemandeEmprunt;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.repository.DemandeEmpruntRepository;
import com.booknest.booknestcore.repository.EmpruntRepository;
import com.booknest.booknestcore.repository.LivreRepository;
import com.booknest.booknestcore.repository.UtilisateurRepository;
import com.booknest.booknestcore.service.DemandeEmpruntService;
import com.booknest.booknestcore.service.LivreService;
import com.booknest.booknestcore.service.UtilisateurService;
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
        return emprunts.stream().map(DemandeEmpruntMapper.INSTANCE::toDto).collect(Collectors.toList());

    }

    @Override
    public void creerDemandeEmprunt(Long livreId, String nom, LocalDate dateEmprunt, LocalDate dateRetour) {
        DemandeEmprunt demande = new DemandeEmprunt();

        // Récupérez les objets Livre et Utilisateur depuis les services ou repositories
        LivreDTO livre = livreService.getLivreById(livreId);
        UtilisateurDTO utilisateur = utilisateurService.getUtilisateurByNom(nom);

        demande.setLivre(empruntMapper.mapLivreFromId(livre.getId()));
        demande.setUtilisateur(empruntMapper.mapUtilisateurFromId(utilisateur.getId()));
        demande.setDateEmprunt(dateEmprunt);
        demande.setDateRetour(dateRetour);
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
        emprunt.setDateEmprunt(demande.getDateEmprunt());
        emprunt.setDateRetour(demande.getDateRetour());
        emprunt.setUtilisateur(demande.getUtilisateur());
        empruntRepository.save(emprunt);
    }

    public void refuserDemande(Long demandeId) {
        DemandeEmprunt demande = demandeEmpruntRepository.findById(demandeId).orElseThrow();
        demande.setStatut(StatutDemande.REFUSE);
        demandeEmpruntRepository.save(demande);
    }
}
