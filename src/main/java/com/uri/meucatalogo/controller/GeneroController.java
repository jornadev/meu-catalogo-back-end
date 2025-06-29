package com.uri.meucatalogo.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.Arrays;
import java.util.List;

@RestController
@RequestMapping("/generos")
public class GeneroController {
    @GetMapping
    public List<String> getGeneros() {
        return Arrays.asList(
            "Ação",
            "Aventura",
            "Comédia",
            "Drama",
            "Fantasia",
            "Ficção Científica",
            "Terror",
            "Romance",
            "Suspense",
            "Animação",
            "Documentário"
        );
    }
}
