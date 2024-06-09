package com.northcoders.recordshopAPI.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class UpdateAlbumDTO {
    String albumName;
    String artistName;
    LocalDate releaseDate;
    Genre genre;
    int priceInPence;
    int quantity;
}
