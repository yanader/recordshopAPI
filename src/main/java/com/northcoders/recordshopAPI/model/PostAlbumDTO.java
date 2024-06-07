package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class PostAlbumDTO {
    String albumName;
    String artistName;
    int priceInPence;
    LocalDate releaseDate;
    Genre genre;
}
