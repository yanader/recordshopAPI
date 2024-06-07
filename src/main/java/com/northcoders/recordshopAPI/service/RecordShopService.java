package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.model.PostAlbumDTO;

import java.util.List;

public interface RecordShopService {
    List<Album> getAllAlbums();
    List<AlbumStockDTO> getAllInStockAlbums();
    AlbumStockDTO getAlbumDTOById(int id);
    Album addAlbum(PostAlbumDTO albumToPost);
}
