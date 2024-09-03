package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.dto.UtilisateurDTO;

import java.util.List;

public interface UtilisateurService {

    List<UtilisateurDTO> getAllUtilisateurs();

    UtilisateurDTO getUtilisateurByNom(String nom);
}
