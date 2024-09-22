package com.booknest.booknestcore.mapper;

import com.booknest.booknestcore.dto.EmpruntDTO;
import com.booknest.booknestcore.model.Emprunt;
import com.booknest.booknestcore.model.Livre;
import com.booknest.booknestcore.model.Utilisateur;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface EmpruntMapper {

    @Mapping(target = "livre", source = "livre.id", qualifiedByName = "mapLivreFromId")
    @Mapping(target = "utilisateur", source = "utilisateur.id", qualifiedByName = "mapUtilisateurFromId")
    Emprunt toEntity(EmpruntDTO dto);

    @Mapping(source = "livre.id", target = "livre.id")
    @Mapping(source = "utilisateur.id", target = "utilisateur.id")
    @Mapping(source = "livre.auteurNom", target = "livre.nomAuteur")
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
