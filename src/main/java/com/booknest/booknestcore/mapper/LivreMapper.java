package com.booknest.booknestcore.mapper;

import com.booknest.booknestcore.dto.LivreDTO;
import com.booknest.booknestcore.model.Livre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

/**
 * Mapper pour convertir entre les entités Livre et LivreDTO.
 * Utilise MapStruct avec composant Spring pour la gestion des mappings.
 */
@Mapper(componentModel = "spring")
public interface LivreMapper {

    // Instance du mapper généré par MapStruct
    LivreMapper INSTANCE = Mappers.getMapper(LivreMapper.class);

    /**
     * Convertit un objet Livre en LivreDTO.
     *
     * @param livre l'objet Livre à convertir
     * @return le LivreDTO correspondant
     */
    @Mapping(target = "nomAuteur", source = "auteurNom")
    LivreDTO livreToLivreDTO(Livre livre);

    /**
     * Convertit un objet LivreDTO en Livre.
     *
     * @param livreDTO l'objet LivreDTO à convertir
     * @return le Livre correspondant
     */
    @Mapping(target = "auteurNom", source = "nomAuteur")
    @Mapping(target = "coverUrl", ignore = true)
    Livre livreDTOToLivre(LivreDTO livreDTO);



}
