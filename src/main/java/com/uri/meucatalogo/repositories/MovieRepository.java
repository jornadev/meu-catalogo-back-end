package com.uri.meucatalogo.repositories;

import com.uri.meucatalogo.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
