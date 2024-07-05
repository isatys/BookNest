package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
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
    @Mapping(target = "auteur.livres", expression = "java(null)")
    LivreDTO livreToLivreDTO(Livre livre);

    /**
     * Convertit un objet LivreDTO en Livre.
     *
     * @param livreDTO l'objet LivreDTO à convertir
     * @return le Livre correspondant
     */
    @Mapping(target = "auteur.livres", expression = "java(null)")
    Livre livreDTOToLivre(LivreDTO livreDTO);
}
