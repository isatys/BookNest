package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.Enum.StatutDemande;
import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.dto.EmpruntDTO;
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
import java.util.Comparator;
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

    @Autowired
    private DemandeEmpruntMapper demandeEmruntMapper;




    public DemandeEmprunt findById(Long demandeId) {
        return demandeEmpruntRepository.findById(demandeId)
                .orElseThrow(() -> new IllegalArgumentException("Demande non trouvée."));
    }


    @Override
    public List<DemandeEmpruntDTO> getAllDemandeEmprunts(){
        List<DemandeEmprunt> emprunts = demandeEmpruntRepository.findAll();
        return emprunts.stream()
                .map(DemandeEmpruntMapper.INSTANCE::toDto)
                .sorted(Comparator.comparing(DemandeEmpruntDTO::getDateEmprunt))
                .collect(Collectors.toList());

    }

    @Override
    public void creerDemandeEmprunt(DemandeEmpruntDTO demandeEmpruntDTO) {

        DemandeEmprunt demande = demandeEmruntMapper.toEntity(demandeEmpruntDTO);

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
