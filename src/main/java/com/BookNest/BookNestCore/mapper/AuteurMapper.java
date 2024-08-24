package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.AuteurDTO;
import com.BookNest.BookNestCore.model.Auteur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface AuteurMapper {
    AuteurMapper INSTANCE = Mappers.getMapper(AuteurMapper.class);


    AuteurDTO auteurToAuteurDTO(Auteur auteur);

    @Mapping(target = "livres", ignore = true) // Ignorez la propriété "livres"
    Auteur auteurDTOToAuteur(AuteurDTO auteurDTO);



}
