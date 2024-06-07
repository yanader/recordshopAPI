package com.northcoders.recordshopAPI.model;

import lombok.Data;

@Data
public class AlbumStockDTO {
    String albumName;
    String artistName;
    int quantity;
}
