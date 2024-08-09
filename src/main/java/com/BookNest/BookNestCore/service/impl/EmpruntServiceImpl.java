package com.BookNest.BookNestCore.service.impl;

import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.mapper.EmpruntMapper;
import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.repository.EmpruntRepository;
import com.BookNest.BookNestCore.repository.LivreRepository;
import com.BookNest.BookNestCore.service.EmpruntService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class EmpruntServiceImpl implements EmpruntService {

    @Autowired
    private EmpruntRepository empruntRepository;

    @Autowired
    private EmpruntMapper empruntMapper;

    @Autowired
    private LivreRepository livreRepository;

    @Override
    public List<EmpruntDTO> getAllEmprunts() {
        return empruntRepository.findAll().stream().map(empruntMapper::toDto).collect(Collectors.toList());
    }

    @Override
    public EmpruntDTO getEmpruntById(Long id) {
        Emprunt emprunt = empruntRepository.findById(id).orElseThrow(() -> new RuntimeException("Emprunt not found"));
        return empruntMapper.toDto(emprunt);
    }

    @Override
    public void createEmprunt(EmpruntDTO empruntDTO) {
        Emprunt emprunt = empruntMapper.toEntity(empruntDTO);
        empruntRepository.save(emprunt);
    }

    @Override
    public void updateEmprunt(Long id, EmpruntDTO empruntDTO) {
        Emprunt emprunt = empruntRepository.findById(id).orElseThrow(() -> new RuntimeException("Emprunt not found"));
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
}
