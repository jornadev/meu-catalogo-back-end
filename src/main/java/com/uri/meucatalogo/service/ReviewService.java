package com.uri.meucatalogo.service;

import com.uri.meucatalogo.models.Comment;
import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.repositories.ReviewRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Iterator;
import java.util.List;
import java.util.UUID;

@Service
public class ReviewService {

    private final ReviewRepository reviewRepository;
    private final MovieService movieService;

    @Autowired
    public ReviewService(ReviewRepository reviewRepository, MovieService movieService) {
        this.reviewRepository = reviewRepository;
        this.movieService = movieService;
    }

    public Review saveReview(Review review) {
        Review saved = reviewRepository.save(review);
        movieService.updateAverageRating(review.getMovieId());
        return saved;
    }

    public List<Review> getReviewsByMovieId(String movieId) {
        return reviewRepository.findByMovieId(movieId);
    }

    public List<Review> getReviewsByUsername(String username) {
        return reviewRepository.findByUsername(username);
    }

    public Review getReviewById(String id) {
        return reviewRepository.findById(id).orElse(null);
    }

    public void deleteReviewById(String id) {
        reviewRepository.deleteById(id);
    }

    public Review addCommentToReview(String reviewId, Comment comment) {
        Review review = getReviewById(reviewId);
        if (review == null) throw new RuntimeException("Review não encontrada");
        if (review.getComentarios() == null) review.setComentarios(new java.util.ArrayList<>());
        comment.setDataCriacao(java.time.LocalDateTime.now());
        if (comment.getId() == null || comment.getId().isEmpty()) {
            comment.setId(UUID.randomUUID().toString());
        }
        review.getComentarios().add(comment);
        return reviewRepository.save(review);
    }

    public Review deleteComment(String reviewId, String commentId, String username, boolean isAdmin) {
        Review review = getReviewById(reviewId);
        if (review == null || review.getComentarios() == null) throw new RuntimeException("Review ou comentário não encontrado");
        Iterator<Comment> it = review.getComentarios().iterator();
        while (it.hasNext()) {
            Comment c = it.next();
            if (c.getId().equals(commentId)) {
                if (!c.getAutor().equals(username) && !isAdmin) throw new RuntimeException("Apenas o autor ou um ADMIN pode excluir o comentário");
                it.remove();
                return reviewRepository.save(review);
            }
        }
        throw new RuntimeException("Comentário não encontrado");
    }

    public Review updateComment(String reviewId, String commentId, String texto, String username, boolean isAdmin) {
        Review review = getReviewById(reviewId);
        if (review == null || review.getComentarios() == null) throw new RuntimeException("Review ou comentário não encontrado");
        for (Comment c : review.getComentarios()) {
            if (c.getId().equals(commentId)) {
                if (!c.getAutor().equals(username) && !isAdmin) throw new RuntimeException("Apenas o autor ou um ADMIN pode editar o comentário");
                c.setTexto(texto);
                return reviewRepository.save(review);
            }
        }
        throw new RuntimeException("Comentário não encontrado");
    }

    public boolean existsByMovieIdAndUsername(String movieId, String username) {
        return reviewRepository.findByMovieId(movieId).stream().anyMatch(r -> r.getUsername().equals(username));
    }

    public List<Review> getReviewsByMovieIdOrdered(String movieId, String username) {
        List<Review> all = reviewRepository.findByMovieId(movieId);
        if (username == null) return all;
        List<Review> result = new java.util.ArrayList<>();
        for (Review r : all) {
            if (r.getUsername().equals(username)) result.add(r);
        }
        for (Review r : all) {
            if (!r.getUsername().equals(username)) result.add(r);
        }
        return result;
    }
}
