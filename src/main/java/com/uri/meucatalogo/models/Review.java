package com.uri.meucatalogo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "reviews")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Review {
    @Id
    private String id;
    private String autor;
    private int nota;
    private String comentario;
    private String movieId;
}


