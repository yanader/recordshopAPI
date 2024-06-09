package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class UpdateAlbumDTO {
    String albumName;
    String artistName;
    LocalDate releaseDate;
    Genre genre;
    Integer priceInPence;
    Integer quantity;
}
