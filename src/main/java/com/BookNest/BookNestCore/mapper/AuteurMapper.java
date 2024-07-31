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

    @Mappings({
            @Mapping(target = "livres", ignore = true) // Prevent recursive mapping
    })
    AuteurDTO auteurToAuteurDTO(Auteur auteur);

    @Mappings({
            @Mapping(target = "livres", ignore = true) // Prevent recursive mapping
    })
    Auteur auteurDTOToAuteur(AuteurDTO auteurDTO);

    List<AuteurDTO> auteursToAuteurDTOs(List<Auteur> auteurs);

    List<Auteur> auteurDTOsToAuteurs(List<AuteurDTO> auteurDTOs);

    @Mappings({
            @Mapping(target = "auteur", ignore = true) // Prevent recursive mapping
    })
    LivreDTO livreToLivreDTO(Livre livre);

    List<LivreDTO> livreListToLivreDTOList(List<Livre> livres);

    @Mappings({
            @Mapping(target = "auteur", ignore = true) // Prevent recursive mapping
    })
    Livre livreDTOToLivre(LivreDTO livreDTO);
}
