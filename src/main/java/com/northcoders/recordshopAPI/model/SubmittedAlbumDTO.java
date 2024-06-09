package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDate;

@Data
@AllArgsConstructor
public class SubmittedAlbumDTO {
    String albumName;
    String artistName;
    Integer priceInPence;
    LocalDate releaseDate;
    Genre genre;
}
