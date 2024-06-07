package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.model.Artist;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
import com.northcoders.recordshopAPI.repository.ArtistRepository;
import com.northcoders.recordshopAPI.repository.StockRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordShopServiceImplTest {

    @Mock
    AlbumRepository mockAlbumRepository;

    @Mock
    ArtistRepository mockArtistRepository;

    @Mock
    StockRepository mockStockRepository;

    @InjectMocks
    RecordShopServiceImpl service;


    @Test
    void getAllAlbums() {
        List<Album> albumList = List.of(
                new Album("Nevermind", new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK ),
                new Album("Owls", new Artist("Owls"), LocalDate.now(), Album.Genre.JAZZ ),
                new Album("One More Time", new Artist("Britney Spears"), LocalDate.now(), Album.Genre.POP )
        );

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<Album> resultList = service.getAllAlbums();

        assertEquals(3, resultList.size());
        assertEquals("Nirvana", resultList.get(0).getArtist().getName());
        assertEquals("Owls", resultList.get(1).getArtist().getName());
        assertEquals("Britney Spears", resultList.get(2).getArtist().getName());
    }

    @Test
    void getAllInStockItems(){
        List<AlbumStockDTO> inStockAlbums = List.of(
                new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2 , 12.99),
                new AlbumStockDTO(2L,"Owls", "Owls", 4, 14.99),
                new AlbumStockDTO(3L, "One More Time", "Britney Spears", 1, 24.99)
        );

        when(mockAlbumRepository.findAlbumsInStock()).thenReturn(inStockAlbums);

        List<AlbumStockDTO> resultList = service.getAllInStockAlbums();

        assertAll(() -> {
            assertEquals(3, resultList.size());
            assertEquals("Nirvana", resultList.get(0).getArtistName());
            assertEquals("Owls", resultList.get(1).getArtistName());
            assertEquals("Britney Spears", resultList.get(2).getArtistName());

            assertTrue(resultList.get(0).getQuantity() > 0);
            assertTrue(resultList.get(1).getQuantity() > 0);
            assertTrue(resultList.get(2).getQuantity() > 0);
        });
        verify(mockAlbumRepository, times(1)).findAlbumsInStock();
    }

//    @Test
//    void getalbumByIdWhenGivenValidId() {
//        Optional<Album> optionalAlbum = Optional.of(new Album("Nevermind", new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK));
//
//        when(mockAlbumRepository.findById(1)).thenReturn(optionalAlbum);
//
//        Optional<Album> actualOptional = service.getAlbumById(1);
//        Album actualAlbum = actualOptional.get();
//
//        assertAll(() -> {
//            assertEquals("Nevermind", actualAlbum.getName());
//            assertEquals("Nirvana", actualAlbum.getArtist().getName());
//            assertEquals(Album.Genre.ROCK, actualAlbum.getName());
//        });
//
//        verify(mockAlbumRepository, times(1)).findById(1);
//
//    }


}