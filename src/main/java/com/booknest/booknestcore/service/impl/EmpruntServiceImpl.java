package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.dto.EmpruntDTO;
import com.booknest.booknestcore.mapper.EmpruntMapper;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.model.Livre;
import com.booknest.booknestcore.repository.EmpruntRepository;
import com.booknest.booknestcore.repository.LivreRepository;
import com.booknest.booknestcore.repository.UserRepository;
import com.booknest.booknestcore.service.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    @Autowired
    protected EmpruntRepository empruntRepository;

    @Autowired
    protected EmpruntMapper empruntMapper;

    @Autowired
    protected LivreRepository livreRepository;

    @Autowired
    protected UserRepository userRepository;

    public List<Emprunt> getEmprunts() {
        return empruntRepository.findAll();
    }

    public void retournerLivre(Long empruntId) {
        // Récupérer l'emprunt par ID
        Optional<Emprunt> optionalEmprunt = empruntRepository.findById(empruntId);

        if (optionalEmprunt.isPresent()) {
            Emprunt emprunt = optionalEmprunt.get();

            // Si le livre est retourné on supprime l'emprunt
            empruntRepository.delete(emprunt);
        } else {
            throw new RuntimeException("Emprunt non trouvé");
        }
    }

    @Override
    public List<EmpruntDTO> getAllEmprunts() {
        return empruntRepository.findAll().stream()
                .map(empruntMapper::toDto)
                .collect(Collectors.toList());
    }

    @Override
    public EmpruntDTO getEmpruntById(Long id) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt not found"));
        return empruntMapper.toDto(emprunt);
    }

    @Override
    public void createEmprunt(EmpruntDTO empruntDTO) {
        Emprunt emprunt = empruntMapper.toEntity(empruntDTO);
        empruntRepository.save(emprunt);
    }

    @Override
    public void updateEmprunt(Long id, EmpruntDTO empruntDTO) {
        Emprunt emprunt = empruntRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Emprunt not found"));
        emprunt.setDateEmprunt(empruntDTO.getDateEmprunt());
        emprunt.setDateRetour(empruntDTO.getDateRetour());
        emprunt.setLivre(empruntMapper.mapLivreFromId(empruntDTO.getLivre().getId()));
        emprunt.setUtilisateur(empruntMapper.mapUtilisateurFromId(empruntDTO.getUtilisateur().getId()));
        empruntRepository.save(emprunt);
    }

    @Override
    public void deleteEmprunt(Long id) {
        empruntRepository.deleteById(id);
    }

    public List<Livre> getAvailableBooks() {
        List<Long> empruntsLivreIds = empruntRepository.findAll().stream()
                .map(emprunt -> emprunt.getLivre().getId())
                .collect(Collectors.toList());
        return livreRepository.findAll().stream()
                .filter(livre -> !empruntsLivreIds.contains(livre.getId()))
                .collect(Collectors.toList());
    }

    public List<EmpruntDTO> getEmpruntsByUtilisateur(Long utilisateurId) {
        List<Emprunt> emprunts = empruntRepository.findByUtilisateurId(utilisateurId);
        return emprunts.stream()
                .map(empruntMapper::toDto)
                .collect(Collectors.toList());
    }

    public Emprunt findById(Long id) {
        return empruntRepository.findById(id).orElse(null);
    }
}
