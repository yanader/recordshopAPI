package com.northcoders.recordshopAPI.view;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumDTO;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.model.Genre;
import com.northcoders.recordshopAPI.service.RecordShopService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSystemTest {

    @Mock
    Scanner mockScanner;

    @Mock
    RecordShopService mockService;

    @InjectMocks
    StockSystem stockSystem;

    @Test
    void provideOptionsBuildsStringCorrectly() {
        String result = """
                What would you like to do today?
                \t1. View all albums
                \t2. View all albums with filter
                \t3. View all in-stock albums
                \t4. View album by id
                \t5. Add an album
                \t6. Update an album (whole)
                \t7. Update an album (partial)
                \t8. Delete an album
                \t0. Quit
                """;

        assertEquals(result, stockSystem.provideOptions());
    }

    @Test
    void viewAllAlbumsCallsServiceMethod() {
        List<AlbumDTO> albumDTOList = List.of(
                new AlbumDTO(new Album(1L, "Nevermind", "Nirvana", LocalDate.EPOCH, Genre.ROCK)),
                new AlbumDTO(new Album(1L, "Artpop", "Lady Gaga", LocalDate.EPOCH, Genre.POP))
        );

        when(mockService.getAllAlbums()).thenReturn(albumDTOList);

        stockSystem.viewAllAlbums();
        verify(mockService, times(1)).getAllAlbums();
    }

    @Test
    void viewAllInStockAlbumsCallsServiceMethod() {
        List<AlbumStockDTO> albumStockDTOList = List.of(
                new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2 , 12.99),
                new AlbumStockDTO(2L,"Owls", "Owls", 4, 14.99),
                new AlbumStockDTO(3L, "One More Time", "Britney Spears", 1, 24.99)
        );

        when(mockService.getAllInStockAlbums()).thenReturn(albumStockDTOList);

        stockSystem.viewAllInStockAlbums();
        verify(mockService, times(1)).getAllInStockAlbums();
    }

    @Test
    void viewAlbumByIdCallsService() {
        AlbumStockDTO albumStockDTO = new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2 , 12.99);

        when(mockService.getAlbumDTOById(1)).thenReturn(albumStockDTO);

        stockSystem.viewAlbumById(1L);
        verify(mockService, times(1)).getAlbumDTOById(1);
    }


}