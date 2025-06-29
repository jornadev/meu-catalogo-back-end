package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.service.ReviewService;
import com.uri.meucatalogo.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Gerenciamento de avaliações de filmes")
public class ReviewController {

    private final ReviewService reviewService;
    private final MovieService movieService;

    public ReviewController(ReviewService reviewService, MovieService movieService) {
        this.reviewService = reviewService;
        this.movieService = movieService;
    }

    @PostMapping
    @Operation(summary = "Adicionar avaliação", description = "Adiciona uma nova avaliação para um filme")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Avaliação adicionada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public Review addReview(@RequestBody Review review) {
        return reviewService.saveReview(review);
    }

    @GetMapping("/movie/{movieId}")
    @Operation(summary = "Listar avaliações de um filme", description = "Retorna todas as avaliações de um filme pelo ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de avaliações retornada"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado")
    })
    public List<Review> getReviewsByMovie(@PathVariable String movieId) {
        return reviewService.getReviewsByMovieId(movieId);
    }

    @PostMapping("/movies/{id}/reviews")
    @Operation(summary = "Adicionar avaliação a um filme", description = "Adiciona uma nova avaliação para um filme específico")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Avaliação adicionada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Filme não encontrado"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Review> addReviewToMovie(@PathVariable String id, @RequestBody Review review) {
        System.out.println("ID recebido: " + id);
        System.out.println("Filme encontrado? " + movieService.getMovieById(id).isPresent());
        if (!movieService.getMovieById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        review.setMovieId(id);
        Review saved = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }
}
