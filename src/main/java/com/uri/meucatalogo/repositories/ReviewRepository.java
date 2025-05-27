package com.uri.meucatalogo.repositories;

import com.uri.meucatalogo.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;
import java.util.UUID;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByMovieId(UUID movieId);
}
