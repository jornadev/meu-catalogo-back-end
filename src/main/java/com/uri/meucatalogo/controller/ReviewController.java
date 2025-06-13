package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.service.ReviewService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/reviews")
@Tag(name = "Reviews", description = "Gerenciamento de avaliações de filmes")
public class ReviewController {

    private final ReviewService reviewService;

    public ReviewController(ReviewService reviewService) {
        this.reviewService = reviewService;
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
}
