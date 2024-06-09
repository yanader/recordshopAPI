package com.northcoders.recordshopAPI.model;

import lombok.Data;
import org.apache.commons.text.WordUtils;


import java.time.LocalDate;

@Data
public class AlbumDTO {

    private long albumId;
    private String albumName;
    private String artistName;
    private LocalDate releaseDate;
    private Genre genre;

    public AlbumDTO(Album album) {
        this.albumId = album.getAlbumId();
        this.albumName = WordUtils.capitalizeFully(album.getAlbumName());
        this.artistName = WordUtils.capitalizeFully(album.getArtistName());
        this.releaseDate = album.getReleaseDate();
        this.genre = album.getGenre();
    }
}
