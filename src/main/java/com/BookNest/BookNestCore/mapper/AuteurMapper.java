package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.AuteurDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Mappings;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface AuteurMapper {
    AuteurMapper INSTANCE = Mappers.getMapper(AuteurMapper.class);


    AuteurDTO auteurToAuteurDTO(Auteur auteur);


    Auteur auteurDTOToAuteur(AuteurDTO auteurDTO);


}
