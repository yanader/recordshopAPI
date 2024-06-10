package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

import org.springframework.util.StringUtils;

import java.text.NumberFormat;
import java.util.Locale;

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

    @Override
    public String toString() {
        return "id: " + "\t" + this.albumId + " | " +
                this.albumName + " - " + this.artistName + " | " +
                this.quantity + " - " + getCurrencyFormat();
    }

    private String getCurrencyFormat() {
        return "£" + this.priceInPounds;
    }
}
