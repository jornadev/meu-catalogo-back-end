package com.uri.meucatalogo.repositories;

import com.uri.meucatalogo.models.Movie;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import java.util.List;

public interface MovieRepository extends MongoRepository<Movie, String> {
    @Query("{ 'title': { $regex: ?0, $options: 'i' }, 'gender': { $all: ?1 } }")
    List<Movie> findByTitleAndGenderAll(String titleRegex, List<String> genders);

    @Query("{ 'gender': { $all: ?0 } }")
    List<Movie> findByGenderAll(List<String> genders);

    @Query("{ 'title': { $regex: ?0, $options: 'i' }, 'gender': { $in: ?1 } }")
    List<Movie> findByTitleAndGenderIn(String titleRegex, List<String> genders);

    @Query("{ 'title': { $regex: ?0, $options: 'i' } }")
    List<Movie> findByTitleRegex(String titleRegex);

    @Query("{ 'gender': { $in: ?0 } }")
    List<Movie> findByGenderIn(List<String> genders);
}
