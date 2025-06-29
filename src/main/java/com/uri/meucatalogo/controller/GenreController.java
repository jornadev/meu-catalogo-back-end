package com.uri.meucatalogo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/genres")
public class GenreController {
    @GetMapping
    public ResponseEntity<List<String>> getGenres() {
        List<String> genres = Arrays.asList(
                "Ação", "Aventura", "Comédia", "Drama", "Fantasia", "Terror", "Romance", "Ficção Científica", "Documentário"
        );
        return ResponseEntity.ok(genres);
    }
}
