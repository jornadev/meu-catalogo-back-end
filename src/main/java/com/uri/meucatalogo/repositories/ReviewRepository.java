package com.uri.meucatalogo.repositories;

import com.uri.meucatalogo.models.Review;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.List;

public interface ReviewRepository extends MongoRepository<Review, String> {
    List<Review> findByMovieId(String movieId);

    @Query("{ 'autor': ?0 }")
    List<Review> findByUsername(String username);
}
