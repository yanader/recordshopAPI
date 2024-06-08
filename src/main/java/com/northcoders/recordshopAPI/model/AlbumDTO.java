package com.northcoders.recordshopAPI.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AlbumDTO {

    private long albumId;
    private String albumName;
    private String artistName;
    private LocalDate releaseDate;
    private Genre genre;

    public AlbumDTO(Album album) {
        this.albumId = getAlbumId();
        this.albumName = album.getAlbumName();
        this.artistName = album.getArtistName();
        this.releaseDate = album.getReleaseDate();
        this.genre = album.getGenre();
    }
}
