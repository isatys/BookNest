package com.booknest.booknestcore.mapper;


import com.booknest.booknestcore.dto.DemandeEmpruntDTO;
import com.booknest.booknestcore.model.DemandeEmprunt;
import org.mapstruct.Mapper;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface DemandeEmpruntMapper {

    // Instance du mapper généré par MapStruct
    DemandeEmpruntMapper INSTANCE = Mappers.getMapper(DemandeEmpruntMapper.class);

    DemandeEmprunt toEntity(DemandeEmpruntDTO dto);

    DemandeEmpruntDTO toDto(DemandeEmprunt entity);
}
