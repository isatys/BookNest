package com.BookNest.BookNestCore.service.impl;
import com.BookNest.BookNestCore.dto.AuteurDTO;
import com.BookNest.BookNestCore.mapper.AuteurMapper;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.repository.AuteurRepository;
import com.BookNest.BookNestCore.service.AuteurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

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
    public void updateAuteur(Long id, AuteurDTO auteurDTO) {
        if (auteurRepository.existsById(id)) {
            Auteur auteur = AuteurMapper.INSTANCE.auteurDTOToAuteur(auteurDTO);
            auteur.setId(id);
            auteurRepository.save(auteur);
        } else {
            throw new RuntimeException("Auteur not found with id: " + id);
        }
    }
}

