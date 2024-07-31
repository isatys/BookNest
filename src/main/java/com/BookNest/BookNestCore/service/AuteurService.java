package com.BookNest.BookNestCore.service;

import com.BookNest.BookNestCore.dto.AuteurDTO;

import java.util.List;

public interface AuteurService {

    List<AuteurDTO>  getAllAuthors();

    AuteurDTO getAuthorById(Long id);

    void createAuthor(AuteurDTO auteurDTO);

    void deleteAuthor(Long id);


    void updateAuteur(Long id, AuteurDTO auteurDTO);
}
