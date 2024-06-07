package com.northcoders.recordshopAPI.controller;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
}
