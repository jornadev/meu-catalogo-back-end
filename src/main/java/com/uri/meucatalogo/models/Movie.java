package com.uri.meucatalogo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@Document(collection = "movies")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Movie {
    @Id
    private String id;
    private String title;
    private String description;
    private String imageUrl;
    private int year;
    private double averageRating;
    private List<String> gender;
    private String trailerUrl;

}
