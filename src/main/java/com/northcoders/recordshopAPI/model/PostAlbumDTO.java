package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PostAlbumDTO {
    String albumName;
    String artistName;
    int priceInPence;
}
