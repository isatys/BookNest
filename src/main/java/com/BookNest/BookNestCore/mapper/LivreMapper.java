package com.BookNest.BookNestCore.mapper;

import com.BookNest.BookNestCore.dto.LivreDTO;
import com.BookNest.BookNestCore.model.Auteur;
import com.BookNest.BookNestCore.model.Livre;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

@Mapper(componentModel = "spring")
public interface LivreMapper {
    LivreMapper INSTANCE = Mappers.getMapper(LivreMapper.class);
    @Mapping(target = "auteur.livres",  expression = "java(null)")
    LivreDTO livreToLivreDTO(Livre livre);

    @Mapping(target = "auteur.livres",  expression = "java(null)")
    Livre livreDTOToLivre(LivreDTO livreDTO);
}

