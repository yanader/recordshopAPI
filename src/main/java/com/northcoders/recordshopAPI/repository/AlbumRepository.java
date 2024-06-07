package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    @Query("SELECT new com.northcoders.recordshopAPI.model.AlbumStockDTO(al.albumId, al.name, ar.name, s.numberInStock, s.priceInPence/100) " +
            "FROM Album al JOIN al.artist ar JOIN Stock s ON s.albumId = al.albumId " +
            "WHERE s.numberInStock > 0")
    List<AlbumStockDTO> findAlbumsInStock();

    @Query("SELECT new com.northcoders.recordshopAPI.model.AlbumStockDTO(al.albumId, al.name, ar.name, s.numberInStock, s.priceInPence/100) " +
            "FROM Album al JOIN al.artist ar JOIN Stock s ON s.albumId = al.albumId " +
            "WHERE al.albumId = :id")
    Optional<AlbumStockDTO> getAlbumDTOById(@Param("id") int id);

    @Query("SELECT new com.northcoders.recordshopAPI.model.Album(al.name, ar.name) " +
            "FROM Album al JOIN al.artist ar " +
            "WHERE al.name = :albumName AND ar.name = :artistName")
    Optional<Album> findAlbumByNameAndArtistName(@Param("albumName") String albumName, @Param("artistName") String artistName);
}



