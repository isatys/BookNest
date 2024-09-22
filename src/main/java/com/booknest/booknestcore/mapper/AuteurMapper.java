package com.booknest.booknestcore.mapper;

import com.booknest.booknestcore.dto.AuteurDTO;
import com.booknest.booknestcore.model.Auteur;
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
