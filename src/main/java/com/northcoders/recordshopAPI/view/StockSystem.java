package com.northcoders.recordshopAPI.view;

import com.northcoders.recordshopAPI.model.AlbumDTO;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.service.RecordShopService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Scanner;

public class StockSystem {

    private Scanner scanner;

    @Autowired
    private RecordShopService recordShopService;

    public StockSystem() {this.scanner = new Scanner(System.in);}

    public void viewAllAlbums() {
        List<AlbumDTO> albumList = recordShopService.getAllAlbums();
        for (AlbumDTO album : albumList) {
            System.out.println(album);
        }
    }

    public void viewAllInStockAlbums() {
        List<AlbumStockDTO> albumStockDTOList = recordShopService.getAllInStockAlbums();
        for (AlbumStockDTO album : albumStockDTOList) {
            System.out.println(album);
        }
    }

    public void viewAlbumById(long id) {
        AlbumStockDTO album = recordShopService.getAlbumDTOById((int)id);
        System.out.println(album);
    }

    public String provideOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append("What would you like to do today?\n");
        for (int i = 1; i < Choice.values().length; i++) {
            sb.append("\t").append(Choice.values()[i].getNumber()).append(". ").append(Choice.values()[i].getText()).append("\n");
        }
        sb.append("\t").append(Choice.values()[0].getNumber()).append(". ").append(Choice.values()[0].getText()).append("\n");
        return sb.toString();
    }
}
