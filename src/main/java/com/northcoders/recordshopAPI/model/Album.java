package com.northcoders.recordshopAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long albumId;

    @NotNull
    private String albumName;

    @NotNull
    private String artistName;

    private LocalDate releaseDate;

    private Genre genre;

    public Album(String albumName, String artistName, LocalDate releaseDate, Genre genre) {
        this.albumName = albumName;
        this.artistName = artistName;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public Album(String albumName, String artistName) {
        this.albumName = albumName;
        this.artistName = artistName;
    }


}
