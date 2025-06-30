package com.uri.meucatalogo.service;

import com.uri.meucatalogo.models.Movie;
import com.uri.meucatalogo.models.Review;
import com.uri.meucatalogo.repositories.MovieRepository;
import com.uri.meucatalogo.repositories.ReviewRepository;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class MovieService {
    private final MovieRepository movieRepository;
    private final ReviewRepository reviewRepository;

    public MovieService(MovieRepository movieRepository, ReviewRepository reviewRepository) {
        this.movieRepository = movieRepository;
        this.reviewRepository = reviewRepository;
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
            m.setTrailerUrl(movie.getTrailerUrl());
            return movieRepository.save(m);
        } else {
            throw new RuntimeException("Filme não encontrado");
        }
    }

    public void updateAverageRating(String movieId) {
        List<Review> reviews = reviewRepository.findByMovieId(movieId);
        if (reviews.isEmpty()) return;
        double avg = reviews.stream().mapToInt(Review::getNota).average().orElse(0.0);
        movieRepository.findById(movieId).ifPresent(movie -> {
            movie.setAverageRating(avg);
            movieRepository.save(movie);
        });
    }

    public List<Movie> searchMovies(String title, String gender, boolean matchAll) {
        boolean hasTitle = title != null && !title.isEmpty();
        boolean hasGender = gender != null && !gender.isEmpty();
        List<String> genderList = hasGender ? Arrays.asList(gender.split(",")) : null;
        if (hasTitle && hasGender) {
            if (matchAll) {
                return movieRepository.findByTitleAndGenderAll(
                    ".*" + java.util.regex.Pattern.quote(title) + ".*",
                    genderList
                );
            } else {
                return movieRepository.findByTitleAndGenderIn(
                    ".*" + java.util.regex.Pattern.quote(title) + ".*",
                    genderList
                );
            }
        } else if (hasTitle) {
            return movieRepository.findByTitleRegex(
                ".*" + java.util.regex.Pattern.quote(title) + ".*"
            );
        } else if (hasGender) {
            if (matchAll) {
                return movieRepository.findByGenderAll(genderList);
            } else {
                return movieRepository.findByGenderIn(genderList);
            }
        } else {
            return movieRepository.findAll();
        }
    }
}
