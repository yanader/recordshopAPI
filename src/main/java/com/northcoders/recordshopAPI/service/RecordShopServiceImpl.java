package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
import com.northcoders.recordshopAPI.repository.StockRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RecordShopServiceImpl implements RecordShopService{

    @Autowired
    AlbumRepository albumRepository;

    @Autowired
    StockRepository stockRepository;

    @Override
    public List<AlbumDTO> getAllAlbums() {
        List<Album> albumList = new ArrayList<>();
        albumRepository.findAll().forEach(albumList::add);
        return albumList.stream().map(AlbumDTO::new).collect(Collectors.toList());
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

        Optional<Album> optionalAlbumToAdd = albumRepository.findByAlbumNameAndArtistName(albumToPost.getAlbumName(), albumToPost.getArtistName());
        Optional<Stock> optionalStockToAdd = Optional.empty();

        if (optionalAlbumToAdd.isPresent()) {
            optionalStockToAdd = stockRepository.findAllByAlbumId(optionalAlbumToAdd.get().getAlbumId());
        }

        Album addedAlbum = null;

        if(optionalAlbumToAdd.isEmpty()) {
            addedAlbum = albumRepository.save(new Album(albumToPost.getAlbumName(), albumToPost.getArtistName(), albumToPost.getReleaseDate(), albumToPost.getGenre()));
            stockRepository.save(new Stock(addedAlbum.getAlbumId(), albumToPost.getPriceInPence(), 1));
        } else {
            Stock stock = optionalStockToAdd.get();
            stock.setNumberInStock(stock.getNumberInStock() + 1);
            addedAlbum = optionalAlbumToAdd.get();
            stockRepository.save(stock);
        }
        return addedAlbum;
    }

    private boolean validPostAlbumDTO(PostAlbumDTO postAlbumDTO) {
        if (postAlbumDTO.getAlbumName() == null) return false;
        if (postAlbumDTO.getArtistName() == null) return false;
        if (postAlbumDTO.getPriceInPence() == null) return false;
        if (postAlbumDTO.getReleaseDate() == null) return false;
        if (postAlbumDTO.getGenre() == null) return false;
        return true;
    }

    public String invalidPostMessage() {
        return "Missing Details: albumName, artistName, priceInPence, releaseDate(yyyy-mm-dd), genre(See documentation for list)";
    }


}
