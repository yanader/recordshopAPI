package com.northcoders.recordshopAPI.model;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AlbumStockDTO {
    long albumId;
    String albumName;
    String artistName;
    int quantity;
}
