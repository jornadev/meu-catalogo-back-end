package com.uri.meu_catalogo.repositories;

import com.uri.meu_catalogo.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByMovieId(UUID movieId);
}
