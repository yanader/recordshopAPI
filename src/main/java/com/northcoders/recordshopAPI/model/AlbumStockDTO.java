package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.util.StringUtils;

@Data
public class AlbumStockDTO {
    long albumId;
    String albumName;
    String artistName;
    int quantity;
    double priceInPounds;

    public AlbumStockDTO(long albumId, String albumName, String artistName, int quantity, double priceInPounds) {
        this.albumId = albumId;
        this.albumName = StringUtils.capitalize(albumName);
        this.artistName = StringUtils.capitalize(artistName);
        this.quantity = quantity;
        this.priceInPounds = priceInPounds;
    }
}
