package com.booknest.booknestcore.mapper;

import com.booknest.booknestcore.dto.UtilisateurDTO;
import com.booknest.booknestcore.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface UtilisateurMapper {
    UtilisateurMapper INSTANCE = Mappers.getMapper(UtilisateurMapper.class);
    UtilisateurDTO userToUtilisateurDTO(Utilisateur utilisateur);
}
