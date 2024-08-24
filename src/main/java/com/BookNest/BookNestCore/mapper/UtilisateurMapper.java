package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.UtilisateurDTO;
import com.BookNest.BookNestCore.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    UtilisateurMapper INSTANCE = Mappers.getMapper(UtilisateurMapper.class);
    UtilisateurDTO userToUtilisateurDTO(Utilisateur utilisateur);
}
