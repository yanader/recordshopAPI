package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
import com.northcoders.recordshopAPI.repository.StockRepository;
import jakarta.transaction.Transactional;
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
    public Album addAlbum(SubmittedAlbumDTO albumToPost) {
        if (!submittedAlbumIsValid(albumToPost)) return null;

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

    @Override
    public AlbumStockDTO putAlbum(SubmittedAlbumDTO albumToPut, int idToPutAt) {
        if (!submittedAlbumIsValid(albumToPut)) return null;
        if (!albumRepository.existsById(idToPutAt)) return null;
        Album newAlbum = new Album(idToPutAt, albumToPut.getAlbumName(), albumToPut.getArtistName(), albumToPut.getReleaseDate(), albumToPut.getGenre());
        albumRepository.save(newAlbum);
        Optional<Stock> stock = stockRepository.findAllByAlbumId(idToPutAt);
        if (stock.isPresent()) {
            stock.get().setPriceInPence(albumToPut.getPriceInPence());
        } else {
            stockRepository.save(new Stock(idToPutAt, albumToPut.getPriceInPence(), 1));
        }
        Stock stockItem = stockRepository.findAllByAlbumId(idToPutAt).get();
        AlbumStockDTO albumStockDTO = new AlbumStockDTO(newAlbum.getAlbumId(), newAlbum.getAlbumName(), newAlbum.getArtistName(), stockItem.getNumberInStock(), stockItem.getPriceInPence());
        return albumStockDTO;
    }

    @Override
    @Transactional
    public boolean deleteById(int id) {
        if(!albumRepository.existsById(id)) return false;
        albumRepository.deleteById(id);
        stockRepository.deleteByAlbumId(id);
        return true;
    }

    @Override
    public List<Album> getAllAlbumsByArtistName(String artistName) {
        List<Album> albumList = albumRepository.findByArtistName(artistName.toLowerCase());
        return albumList.isEmpty() ? null : albumList;
    }

    @Override
    public List<Album> getAlbumsByYear(int year) {
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);
        List<Album> albumList = albumRepository.findByReleaseDateBetween(start, end);
        return albumList.isEmpty() ? null : albumList;
    }

    private boolean submittedAlbumIsValid(SubmittedAlbumDTO submittedAlbumDTO) {
        if (submittedAlbumDTO.getAlbumName() == null) return false;
        if (submittedAlbumDTO.getArtistName() == null) return false;
        if (submittedAlbumDTO.getPriceInPence() == null) return false;
        if (submittedAlbumDTO.getReleaseDate() == null) return false;
        if (submittedAlbumDTO.getGenre() == null) return false;
        return true;
    }

    public String invalidPostMessage() {
        return "Missing Details: albumName, artistName, priceInPence, releaseDate(yyyy-mm-dd), genre(See documentation for list)";
    }


}
