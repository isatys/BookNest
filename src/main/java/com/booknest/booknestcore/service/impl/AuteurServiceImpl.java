package com.booknest.booknestcore.service.impl;
import com.booknest.booknestcore.dto.AuteurDTO;
import com.booknest.booknestcore.mapper.AuteurMapper;
import com.booknest.booknestcore.model.Auteur;
import com.booknest.booknestcore.repository.AuteurRepository;
import com.booknest.booknestcore.service.AuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuteurServiceImpl implements AuteurService {

    @Autowired
    private AuteurRepository auteurRepository;

    @Override
    public List<AuteurDTO> getAllAuthors() {
        return auteurRepository.findAll()
                .stream()
                .sorted(Comparator.comparing(Auteur::getNom)) // Tri par ordre alphabétique croissant du nom
                .map(AuteurMapper.INSTANCE::auteurToAuteurDTO)
                .collect(Collectors.toList());
    }

    @Override
    public AuteurDTO getAuthorById(Long id) {
        Auteur auteur = auteurRepository.findById(id).orElse(null);
        if (auteur != null) {
            return AuteurMapper.INSTANCE.auteurToAuteurDTO(auteur);
        } else {
            return null;
        }
    }

    @Override
    @Transactional
    public void createAuthor(AuteurDTO auteurDTO) {
        Auteur auteur = AuteurMapper.INSTANCE.auteurDTOToAuteur(auteurDTO);
        auteurRepository.save(auteur);
    }

    @Override
    @Transactional
    public void deleteAuthor(Long id) {
        auteurRepository.deleteById(id);
    }

    @Override
    @Transactional
    public void updateAuteur(Long id, AuteurDTO auteurDTO) {
        Auteur auteur = auteurRepository.findById(id).orElseThrow(() -> new RuntimeException("Auteur not found"));

        // Mettre à jour uniquement les champs nom et biographie
        auteur.setNom(auteurDTO.getNom());
        auteur.setBiographie(auteurDTO.getBiographie());

        // Ne pas toucher à la collection de livres
        auteurRepository.save(auteur);
    }

    @Override
    public List<AuteurDTO> searchAuthorsByName(String name) {
        return auteurRepository.findByNomContainingIgnoreCase(name)
                .stream()
                .map(AuteurMapper.INSTANCE::auteurToAuteurDTO)
                .collect(Collectors.toList());
    }


}

