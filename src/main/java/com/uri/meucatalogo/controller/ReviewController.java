package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.models.Comment;
import com.uri.meucatalogo.service.ReviewService;
import com.uri.meucatalogo.service.MovieService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.access.prepost.PreAuthorize;

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
    @Operation(summary = "Listar avaliações de um filme", description = "Retorna todas as avaliações de um filme pelo ID, priorizando a do usuário autenticado no topo.")
    public List<Review> getReviewsByMovie(@PathVariable String movieId) {
        String username = null;
        try {
            username = SecurityContextHolder.getContext().getAuthentication().getName();
        } catch (Exception ignored) {}
        return reviewService.getReviewsByMovieIdOrdered(movieId, username);
    }

    @PostMapping("/movies/{id}/reviews")
    @Operation(summary = "Adicionar avaliação a um filme", description = "Adiciona uma nova avaliação para um filme específico. Usuário só pode avaliar uma vez por filme.")
    public ResponseEntity<Review> addReviewToMovie(@PathVariable String id, @RequestBody Review review) {
        if (!movieService.getMovieById(id).isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).build();
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        if (reviewService.existsByMovieIdAndUsername(id, username)) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
        }
        review.setUsername(username);
        review.setMovieId(id);
        Review saved = reviewService.saveReview(review);
        return ResponseEntity.status(HttpStatus.CREATED).body(saved);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{id}")
    @Operation(summary = "Deletar avaliação", description = "Permite ao autor ou ADMIN deletar uma avaliação.")
    public ResponseEntity<Void> deleteReview(@PathVariable String id) {
        Review review = reviewService.getReviewById(id);
        if (review == null) {
            return ResponseEntity.notFound().build();
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!review.getUsername().equals(username) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        reviewService.deleteReviewById(id);
        return ResponseEntity.noContent().build();
    }

    @PreAuthorize("isAuthenticated()")
    @PostMapping("/{reviewId}/comments")
    @Operation(summary = "Adicionar comentário a uma avaliação", description = "Adiciona um comentário a uma avaliação de filme. Requer autenticação.")
    @ApiResponses({
        @ApiResponse(responseCode = "201", description = "Comentário adicionado com sucesso"),
        @ApiResponse(responseCode = "404", description = "Avaliação não encontrada"),
        @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<Review> addCommentToReview(@PathVariable String reviewId, @RequestBody Comment comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        comment.setAutor(username);
        Review updated = reviewService.addCommentToReview(reviewId, comment);
        return ResponseEntity.status(HttpStatus.CREATED).body(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @DeleteMapping("/{reviewId}/comments/{commentId}")
    @Operation(summary = "Excluir comentário de uma avaliação", description = "Permite ao autor ou um ADMIN excluir um comentário de uma avaliação.")
    public ResponseEntity<Review> deleteComment(@PathVariable String reviewId, @PathVariable String commentId) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Review updated = reviewService.deleteComment(reviewId, commentId, username, isAdmin);
        return ResponseEntity.ok(updated);
    }

    @PutMapping("/{id}")
    @Operation(summary = "Editar avaliação", description = "Permite ao autor ou ADMIN editar uma avaliação de filme.")
    public ResponseEntity<Review> updateReview(@PathVariable String id, @RequestBody Review review) {
        Review existing = reviewService.getReviewById(id);
        if (existing == null) {
            return ResponseEntity.notFound().build();
        }
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        if (!existing.getUsername().equals(username) && !isAdmin) {
            return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
        }
        existing.setNota(review.getNota());
        existing.setComentario(review.getComentario());
        Review updated = reviewService.saveReview(existing);
        return ResponseEntity.ok(updated);
    }

    @PreAuthorize("isAuthenticated()")
    @PutMapping("/{reviewId}/comments/{commentId}")
    @Operation(summary = "Editar comentário de uma avaliação", description = "Permite ao autor ou ADMIN editar um comentário em uma avaliação.")
    public ResponseEntity<Review> updateComment(@PathVariable String reviewId, @PathVariable String commentId, @RequestBody Comment comment) {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        boolean isAdmin = SecurityContextHolder.getContext().getAuthentication().getAuthorities().stream().anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
        Review updated = reviewService.updateComment(reviewId, commentId, comment.getTexto(), username, isAdmin);
        return ResponseEntity.ok(updated);
    }
}
