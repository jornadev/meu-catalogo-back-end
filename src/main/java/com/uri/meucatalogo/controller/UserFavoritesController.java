package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.FavoriteMovie;
import com.uri.meucatalogo.service.UserFavoritesService;
import com.uri.meucatalogo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserFavoritesController {
    @Autowired
    private UserFavoritesService userFavoritesService;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{username}/favorites")
    public ResponseEntity<?> getUserFavorites(@PathVariable String username, @RequestHeader("Authorization") String token) {
        try {
            String requester = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            if (!requester.equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado aos favoritos de outro usuário.");
            }
            List<FavoriteMovie> favorites = userFavoritesService.getFavoriteMovies(username);
            return ResponseEntity.ok(favorites);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar favoritos: " + e.getMessage());
        }
    }

    @PostMapping("/favorites/{movieId}")
    public ResponseEntity<?> toggleFavorite(
        @PathVariable String movieId,
        @RequestHeader("Authorization") String token
    ) {
        try {
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            userFavoritesService.toggleFavorite(username, movieId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao atualizar favorito: " + e.getMessage());
        }
    }

    @DeleteMapping("/favorites/{movieId}")
    public ResponseEntity<?> removeFavorite(
        @PathVariable String movieId,
        @RequestHeader("Authorization") String token
    ) {
        try {
            String username = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            userFavoritesService.removeFavorite(username, movieId);
            return ResponseEntity.ok().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao remover favorito: " + e.getMessage());
        }
    }
}
