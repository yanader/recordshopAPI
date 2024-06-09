package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    @Query("SELECT new com.northcoders.recordshopAPI.model.AlbumStockDTO(al.albumId, al.albumName, al.artistName, s.numberInStock, s.priceInPence/100) " +
            "FROM Album al JOIN Stock s ON s.albumId = al.albumId " +
            "WHERE s.numberInStock > 0")
    List<AlbumStockDTO> findAlbumsInStock();

    @Query("SELECT new com.northcoders.recordshopAPI.model.AlbumStockDTO(al.albumId, al.albumName, al.artistName, s.numberInStock, s.priceInPence/100) " +
            "FROM Album al JOIN Stock s ON s.albumId = al.albumId " +
            "WHERE al.albumId = :id")
    Optional<AlbumStockDTO> getAlbumDTOById(@Param("id") int id);

//    @Query("SELECT new com.northcoders.recordshopAPI.model.Album(al.albumName, al.artistName) " +
//            "FROM Album " +
//            "WHERE al.name = :albumName AND ar.name = :artistName")
    Optional<Album> findByAlbumNameAndArtistName(String albumName, String artistName);

    Optional<Album> findByAlbumName(String albumName);

    List<Album> findByArtistName(String artistName);

    List<Album> findByReleaseDateBetween(LocalDate start, LocalDate end);
}



