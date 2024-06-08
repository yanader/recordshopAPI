package com.northcoders.recordshopAPI.model;

import lombok.Data;

import java.util.List;
import java.util.stream.Collectors;

@Data
public class ArtistDTO {
    private long artistId;
    private String name;
    private List<String> discography;

    public ArtistDTO(Artist artist) {
        this.artistId = artist.getArtistId();
        this.name = artist.getName();
        this.discography = artist.getDiscography().stream()
                .map(Album::getName)
                .collect(Collectors.toList());
    }
}

