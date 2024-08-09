package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.AuteurDTO;
import com.BookNest.BookNestCore.dto.EmpruntDTO;
import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Emprunt;
import com.BookNest.BookNestCore.model.Livre;
import com.BookNest.BookNestCore.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

import java.util.List;

@Mapper(componentModel = "spring")
public interface EmpruntMapper {

    @Mapping(target = "livre", source = "livre.id", qualifiedByName = "mapLivreFromId")
    @Mapping(target = "utilisateur", source = "utilisateur.id", qualifiedByName = "mapUtilisateurFromId")
    Emprunt toEntity(EmpruntDTO dto);

    @Mapping(source = "livre.id", target = "livre.id")
    @Mapping(source = "utilisateur.id", target = "utilisateur.id")
    EmpruntDTO toDto(Emprunt entity);

    @Named("mapLivreFromId")
    default Livre mapLivreFromId(Long id) {
        if (id == null) return null;
        return new Livre(id);
    }

    @Named("mapUtilisateurFromId")
    default Utilisateur mapUtilisateurFromId(Long id) {
        if (id == null) return null;
        return new Utilisateur(id);
    }
}
