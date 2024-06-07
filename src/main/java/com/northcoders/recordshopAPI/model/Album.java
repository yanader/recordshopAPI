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
    @GeneratedValue
    private long albumId;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    private LocalDate releaseDate;

    private Genre genre;

    public Album(String name, Artist artist, LocalDate releaseDate, Genre genre) {
        this.name = name;
        this.artist = artist;
        this.releaseDate = releaseDate;
        this.genre = genre;
    }

    public Album(String name, String artistName) {
        this.name = name;
        this.artist = new Artist(artistName);
    }


}
