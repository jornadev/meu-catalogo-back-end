package com.uri.meucatalogo.repositories;

import com.uri.meucatalogo.models.UserFavorites;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFavoritesRepository extends MongoRepository<UserFavorites, String> {
    Optional<UserFavorites> findByUsername(String username);
}
