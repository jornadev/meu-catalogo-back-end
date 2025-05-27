package com.uri.meu_catalogo.repositories;

import com.uri.meu_catalogo.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface MovieRepository extends MongoRepository<Movie, String> {
}
