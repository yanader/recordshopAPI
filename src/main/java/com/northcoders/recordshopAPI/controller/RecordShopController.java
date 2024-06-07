package com.northcoders.recordshopAPI.controller;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/recordstore")
public class RecordShopController {

    @Autowired
    RecordShopService recordShopService;

    @GetMapping(value = "/albums")
    public ResponseEntity<List<Album>> getAllAlbums() {
        return new ResponseEntity<>(recordShopService.getAllAlbums() , HttpStatus.OK);
    }

    @GetMapping(value = "/instock")
    public ResponseEntity<List<AlbumStockDTO>> getAllInStockAlbums() {
        List<AlbumStockDTO> inStockAlbums = recordShopService.getAllInStockAlbums();
        if (inStockAlbums.isEmpty()) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "There are no albums in stock");
        } else {
            return new ResponseEntity<>(inStockAlbums, HttpStatus.OK);
        }
    }

    @GetMapping(value ="/albums/{id}")
    public ResponseEntity<AlbumStockDTO> getAlbumDTOById(@PathVariable int id) {
        AlbumStockDTO album = recordShopService.getAlbumDTOById(id);
        if (album == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No album with id " + id + " exists");
        } else {
            return new ResponseEntity<>(album, HttpStatus.OK);
        }
    }
}
