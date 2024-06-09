package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.*;

import java.util.List;

public interface RecordShopService {
    List<AlbumDTO> getAllAlbums();
    List<AlbumStockDTO> getAllInStockAlbums();
    AlbumStockDTO getAlbumDTOById(int id);
    Album addAlbum(SubmittedAlbumDTO albumToPost);
    String invalidPostMessage();
    AlbumStockDTO putAlbum(SubmittedAlbumDTO albumToPut, int idToPutAt);
    boolean deleteById(int id);
    List<Album> getAllAlbumsByArtistName(String artistName);
    List<Album> getAlbumsByYear(int year);
    List<Album> getAlbumsByGenre(String genre);
    AlbumStockDTO getAlbumDetailsByAlbumName(String albumName);
    Album updateAlbumDetails(long id, UpdateAlbumDTO updates);
}
