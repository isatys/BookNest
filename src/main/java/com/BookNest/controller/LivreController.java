package com.BookNest.controller;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;
import com.BookNest.model.Livre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.BookNest.service.LivreService;

import java.util.List;

@RestController
@Api(value = "Gestion des livres", description = "Opérations pour la gestion des livres")
public class LivreController {

    @Autowired
    private LivreService livreService;

    @GetMapping
    @ApiOperation(value = "Obtenir tous les livres", notes = "Récupère tous les livres disponibles dans la base de données")
    public ResponseEntity<List<Livre>> getAllLivres() {
        List<Livre> livres = livreService.getAllLivres();
        return new ResponseEntity<>(livres, HttpStatus.OK);
    }

    @ApiOperation(value = "Get a book by ID", notes = "Retrieves a book based on its ID")
    @ApiResponses(value = {
            @ApiResponse(code = 200, message = "Successfully retrieved book"),
            @ApiResponse(code = 404, message = "Book not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<Livre> getLivreById(@PathVariable Long id) {
        Livre livre= livreService.getLivreById(id);
        return new ResponseEntity<>(livre, HttpStatus.OK);
    }
    @ApiOperation(value = "Save a book ", notes = "Save a book  OK")
    @PostMapping("/save")
    public ResponseEntity<Livre> saveLivre(@RequestBody Livre livre) {
        Livre savedLivre = livreService.saveLivre(livre);
        return new ResponseEntity<>(savedLivre, HttpStatus.CREATED);
    }
}
