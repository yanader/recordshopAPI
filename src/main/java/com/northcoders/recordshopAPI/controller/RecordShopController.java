package com.northcoders.recordshopAPI.controller;

import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.service.RecordShopService;
import org.apache.coyote.Response;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@RestController
@RequestMapping("api/v1/recordstore")
public class RecordShopController {

    @Autowired
    RecordShopService recordShopService;

    @GetMapping(value = "/albums")
    public ResponseEntity<List<AlbumDTO>> getAllAlbums() {
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

    @PostMapping(value ="/albums/add")
    public ResponseEntity<Album> addNewAlbum(@RequestBody SubmittedAlbumDTO albumToAdd) {
        Album addedAlbum = recordShopService.addAlbum(albumToAdd);
        if (addedAlbum == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, recordShopService.invalidPostMessage());
        } else {
            return new ResponseEntity<>(addedAlbum, HttpStatus.CREATED);
        }
    }

    @PutMapping(value="albums/{id}")
    public ResponseEntity<AlbumStockDTO> changeAlbum(@RequestBody SubmittedAlbumDTO albumToPut, @PathVariable int id) {
        AlbumStockDTO albumStockDTO = recordShopService.putAlbum(albumToPut, id);
        if (albumStockDTO == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Error: Missing body or missing albumID");
        } else {
            return new ResponseEntity<>(albumStockDTO, HttpStatus.CREATED);
        }
    }

    @DeleteMapping(value="albums/{id}")
    public ResponseEntity<String> deleteAlbum(@PathVariable int id) {
        boolean result = recordShopService.deleteById(id);
        if (result) {
            return new ResponseEntity<>("Id " + id + " successfully deleted", HttpStatus.ACCEPTED);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Deletion failed. No record at id " + id);
        }
    }

    @GetMapping(value="/albums", params ="artistname")
    public ResponseEntity<List<Album>> getAllAlbumsByArtist(@RequestParam String artistname) {
        List<Album> albumList = recordShopService.getAllAlbumsByArtistName(artistname);
        if (albumList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We have no albums by " + artistname);
        } else {
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        }
    }

    @GetMapping(value="/albums", params="year")
    public ResponseEntity<List<Album>> getAllAlbumsByYear(@RequestParam int year) {
        List<Album> albumList = recordShopService.getAlbumsByYear(year);
        if (albumList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We have no albums from " + year);
        } else {
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        }
    }

    @GetMapping(value="/albums", params="genre")
    public ResponseEntity<List<Album>> getAllAlbumsFromGenre(@RequestParam String genre) {
        List<Album> albumList = recordShopService.getAlbumsByGenre(genre.toUpperCase());
        if (albumList == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "We have no albums in genre: " + genre.toUpperCase());
        } else {
            return new ResponseEntity<>(albumList, HttpStatus.OK);
        }
    }

    @GetMapping(value="/albums", params="name")
    public ResponseEntity<AlbumStockDTO> getAlbumByName(@RequestParam String name) {
        AlbumStockDTO albumStockDTO = recordShopService.getAlbumDetailsByAlbumName(name.toLowerCase());
        if (albumStockDTO == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No album called " + name + " in stock");
        } else {
            return new ResponseEntity<>(albumStockDTO, HttpStatus.OK);
        }
    }

    @PatchMapping(value="/albums/{id}")
    public ResponseEntity<Album> updateAlbumAndStock(@PathVariable int id, @RequestBody UpdateAlbumDTO updates) {
        Album updatedAlbum = recordShopService.updateAlbumDetails(id, updates);
        if (updatedAlbum == null) {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No album with id " + id + " exists");
        } else {
            return new ResponseEntity<>(updatedAlbum, HttpStatus.OK);
        }
    }

}
