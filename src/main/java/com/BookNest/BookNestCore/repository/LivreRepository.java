package com.BookNest.BookNestCore.repository;


import com.BookNest.BookNestCore.model.Livre;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;


@Repository
public interface LivreRepository extends JpaRepository<Livre, Long> {
}
