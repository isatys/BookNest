package com.BookNest.BookNestCore.mapper;


import com.BookNest.BookNestCore.dto.DemandeEmpruntDTO;
import com.BookNest.BookNestCore.model.DemandeEmprunt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DemandeEmpruntMapper {

    // Instance du mapper généré par MapStruct
    DemandeEmpruntMapper INSTANCE = Mappers.getMapper(DemandeEmpruntMapper.class);

    DemandeEmprunt toEntity(DemandeEmpruntDTO dto);

    DemandeEmpruntDTO toDto(DemandeEmprunt entity);
}
