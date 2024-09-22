package com.booknest.booknestcore.service;

import com.booknest.booknestcore.dto.UtilisateurDTO;

import java.util.List;

public interface UtilisateurService {

    List<UtilisateurDTO> getAllUtilisateurs();

    UtilisateurDTO getUtilisateurByNom(String nom);
}
