package com.uri.meucatalogo.service;

import com.uri.meucatalogo.models.Movie;
import com.uri.meucatalogo.repositories.MovieRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class MovieService {

    private final MovieRepository movieRepository;

    public MovieService(MovieRepository movieRepository) {
        this.movieRepository = movieRepository;
    }

    public Movie saveMovie(Movie movie) {
        return movieRepository.save(movie);
    }

    public List<Movie> getAllMovies() {
        return movieRepository.findAll();
    }

    public Optional<Movie> getMovieById(String id) {
        return movieRepository.findById(id);
    }

    public void deleteMovieById(String id) {
        movieRepository.deleteById(id);
    }

    public Movie updateMovie(String id, Movie movie) {
        Optional<Movie> existing = movieRepository.findById(id);
        if (existing.isPresent()) {
            Movie m = existing.get();
            m.setTitle(movie.getTitle());
            m.setDescription(movie.getDescription());
            m.setImageUrl(movie.getImageUrl());
            m.setYear(movie.getYear());
            m.setAverageRating(movie.getAverageRating());
            m.setGender(movie.getGender());
            return movieRepository.save(m);
        } else {
            throw new RuntimeException("Filme não encontrado");
        }
    }
}
