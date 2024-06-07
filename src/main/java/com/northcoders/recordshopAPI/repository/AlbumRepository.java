package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    @Query("SELECT new com.northcoders.recordshopAPI.model.AlbumStockDTO(al.albumId, al.name, ar.name, s.numberInStock) " +
            "FROM Album al JOIN al.artist ar JOIN Stock s ON s.albumId = al.albumId " +
            "WHERE s.numberInStock > 0")
    List<AlbumStockDTO> findAlbumsInStock();
}


// @Query("SELECT a FROM Album a WHERE a.id IN (SELECT s.albumId FROM Stock s WHERE s.quantity > 0)")
//
