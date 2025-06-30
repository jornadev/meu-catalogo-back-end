package com.uri.meucatalogo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import java.util.List;

@Document(collection = "user_favorites")
@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class UserFavorites {
    @Id
    private String id;
    private String username;
    private List<FavoriteMovie> favoriteMovies;
}
