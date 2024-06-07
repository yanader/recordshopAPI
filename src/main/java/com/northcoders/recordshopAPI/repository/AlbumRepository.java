package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Album;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Map;

@Repository
public interface AlbumRepository extends CrudRepository<Album, Integer> {

    @Query("""
            SELECT al.name, ar.name, s.number_in_stock
            FROM album al
            JOIN artist ar on ar.artist_id = al.artist_id
            JOIN stock s on s.album_id = al.album_id
            WHERE s.quantity > 0;
            """)
    Map<AlbumStockDTO> findAlbumsInStock();
}


// @Query("SELECT a FROM Album a WHERE a.id IN (SELECT s.albumId FROM Stock s WHERE s.quantity > 0)")
//
