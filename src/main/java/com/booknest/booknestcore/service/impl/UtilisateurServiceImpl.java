package com.booknest.booknestcore.service.impl;

import com.booknest.booknestcore.dto.UtilisateurDTO;
import com.booknest.booknestcore.mapper.UtilisateurMapper;
import com.booknest.booknestcore.model.Utilisateur;
import com.booknest.booknestcore.repository.UtilisateurRepository;
import com.booknest.booknestcore.service.UtilisateurService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UtilisateurServiceImpl implements UtilisateurService {

    @Autowired
    private UtilisateurRepository utilisateurRepository;

    @Autowired
    private UtilisateurMapper utilisateurMapper;

    public List<UtilisateurDTO> getAllUtilisateurs() {
        List<Utilisateur> utilisateurs = utilisateurRepository.findAll();
        return utilisateurs.stream()
                .map(UtilisateurMapper.INSTANCE::userToUtilisateurDTO)
                .collect(Collectors.toList());
    }

    public UtilisateurDTO getUtilisateurByNom(String nom) {
        Optional<Utilisateur> utilisateur = utilisateurRepository.findByNom(nom);
        return utilisateur.map(utilisateurMapper::userToUtilisateurDTO).orElse(null);
    }
}
