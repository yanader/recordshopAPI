package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
import com.northcoders.recordshopAPI.repository.ArtistRepository;
import com.northcoders.recordshopAPI.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RecordShopServiceImpl implements RecordShopService{

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    ArtistRepository artistRepository;

    @Autowired
    StockRepository stockRepository;

    @Override
    public List<Album> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumRepository.findAll().forEach(albumList::add);
        return albumList;
    }
}
