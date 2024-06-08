package com.northcoders.recordshopAPI.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumDTO {

    private long albumId;
    private String name;
    private String artist;
    private LocalDate releaseDate;
    private Genre genre;

    public AlbumDTO(Album album) {
        this.albumId = getAlbumId();
        this.name = album.getName();
        this.artist = album.getArtist().getName();
        this.releaseDate = album.getReleaseDate();
        this.genre = album.getGenre();
    }
}
