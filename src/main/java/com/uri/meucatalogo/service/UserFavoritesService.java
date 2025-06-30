package com.uri.meucatalogo.service;

import com.uri.meucatalogo.models.FavoriteMovie;
import com.uri.meucatalogo.models.Movie;
import com.uri.meucatalogo.models.UserFavorites;
import com.uri.meucatalogo.repositories.MovieRepository;
import com.uri.meucatalogo.repositories.UserFavoritesRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserFavoritesService {
    @Autowired
    private UserFavoritesRepository userFavoritesRepository;
    @Autowired
    private MovieRepository movieRepository;

    public List<FavoriteMovie> getFavoriteMovies(String username) {
        UserFavorites userFavorites = userFavoritesRepository.findByUsername(username)
            .orElse(new UserFavorites(null, username, new ArrayList<>()));
        return userFavorites.getFavoriteMovies();
    }

    public void toggleFavorite(String username, String movieId) {
        UserFavorites userFavorites = userFavoritesRepository.findByUsername(username)
            .orElse(new UserFavorites(null, username, new ArrayList<>()));
        List<FavoriteMovie> favorites = userFavorites.getFavoriteMovies();
        Optional<FavoriteMovie> existing = favorites.stream()
            .filter(f -> f.getMovieId().equals(movieId))
            .findFirst();
        if (existing.isPresent()) {
            favorites.remove(existing.get());
        } else {
            Movie movie = movieRepository.findById(movieId).orElse(null);
            if (movie != null) {
                favorites.add(new FavoriteMovie(
                    movie.getId(),
                    movie.getTitle(),
                    movie.getImageUrl(),
                    movie.getYear()
                ));
            }
        }
        userFavoritesRepository.save(userFavorites);
    }

    public void removeFavorite(String username, String movieId) {
        UserFavorites userFavorites = userFavoritesRepository.findByUsername(username)
            .orElse(new UserFavorites(null, username, new ArrayList<>()));
        List<FavoriteMovie> favorites = userFavorites.getFavoriteMovies();
        favorites.removeIf(f -> f.getMovieId().equals(movieId));
        userFavoritesRepository.save(userFavorites);
    }
}
