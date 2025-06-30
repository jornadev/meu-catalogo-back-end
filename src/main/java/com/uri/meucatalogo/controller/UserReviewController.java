package com.uri.meucatalogo.controller;

import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.repositories.ReviewRepository;
import com.uri.meucatalogo.security.JwtUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/users")
public class UserReviewController {
    @Autowired
    private ReviewRepository reviewRepository;
    @Autowired
    private JwtUtil jwtUtil;

    @GetMapping("/{username}/reviews")
    public ResponseEntity<?> getUserReviews(@PathVariable String username, @RequestHeader("Authorization") String token) {
        try {
            String requester = jwtUtil.extractUsername(token.replace("Bearer ", ""));
            if (!requester.equals(username)) {
                return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Acesso negado às avaliações de outro usuário.");
            }
            List<Review> reviews = reviewRepository.findByUsername(username);
            return ResponseEntity.ok(reviews);
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body("Erro ao buscar avaliações: " + e.getMessage());
        }
    }
}
