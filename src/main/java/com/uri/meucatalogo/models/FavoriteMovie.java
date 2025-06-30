package com.uri.meucatalogo.models;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter @Setter
@NoArgsConstructor @AllArgsConstructor
public class FavoriteMovie {
    private String movieId;
    private String title;
    private String imageUrl;
    private int year;
}
