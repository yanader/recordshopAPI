package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
import com.northcoders.recordshopAPI.repository.ArtistRepository;
import com.northcoders.recordshopAPI.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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

    @Override
    public List<AlbumStockDTO> getAllInStockAlbums() {
        return new ArrayList<>(albumRepository.findAlbumsInStock());
    }

    @Override
    public AlbumStockDTO getAlbumDTOById(int id) {
        Optional<AlbumStockDTO> optional = albumRepository.getAlbumDTOById(id);
        return optional.orElse(null);
    }

    @Override
    public Album addAlbum(PostAlbumDTO albumToPost) {
        if (!validPostAlbumDTO(albumToPost)) return null;

        Optional<Album> optionalAlbumToAdd = albumRepository.findAlbumByNameAndArtistName(albumToPost.getAlbumName(), albumToPost.getArtistName());
        Optional<Artist> optionalArtistToAdd = artistRepository.findByName(albumToPost.getArtistName());
        Optional<Stock> optionalStockToAdd = Optional.empty();
        if (optionalAlbumToAdd.isPresent()) {
            optionalStockToAdd = stockRepository.findAllByAlbumId(optionalAlbumToAdd.get().getAlbumId());
        }

        Album addedAlbum = null;

        if(optionalAlbumToAdd.isEmpty() && optionalArtistToAdd.isEmpty()) {
            Artist artist = artistRepository.save(new Artist(albumToPost.getArtistName()));
            addedAlbum = albumRepository.save(new Album(albumToPost.getAlbumName(), artist, LocalDate.now(), albumToPost.getGenre()));
            stockRepository.save(new Stock(addedAlbum.getAlbumId(), albumToPost.getPriceInPence(), 1));
        } else if(optionalAlbumToAdd.isEmpty()) {
            addedAlbum = albumRepository.save(new Album(albumToPost.getAlbumName(), optionalArtistToAdd.get(), LocalDate.now(), albumToPost.getGenre()));
            stockRepository.save(new Stock(addedAlbum.getAlbumId(), albumToPost.getPriceInPence(), 1));
        } else {
            Stock stock = optionalStockToAdd.get();
            stock.setNumberInStock(stock.getNumberInStock() + 1);
            addedAlbum = optionalAlbumToAdd.get();
        }
        return addedAlbum;
    }

    private boolean validPostAlbumDTO(PostAlbumDTO postAlbumDTO) {
        String albumName = postAlbumDTO.getAlbumName();
        String artistName = postAlbumDTO.getArtistName();
        return albumName != null && artistName != null && !albumName.isEmpty() && !artistName.isEmpty();
    }


}
