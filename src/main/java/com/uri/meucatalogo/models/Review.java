package com.uri.meucatalogo.models;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import java.util.List;

@Document(collection = "reviews")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class Review {
    @Id
    private String id;
    @Field("autor")
    private String username;
    private int nota;
    private String comentario;
    private String movieId;
    private List<Comment> comentarios;

    @JsonProperty("autor")
    public String getAutor() {
        return username;
    }
}


