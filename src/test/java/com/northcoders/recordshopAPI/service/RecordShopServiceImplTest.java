package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.model.Artist;
import com.northcoders.recordshopAPI.model.Stock;
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

    @Test
    void getalbumByIdWhenGivenValidId() {
        Optional<AlbumStockDTO> optionalAlbumDTO = Optional.of(new AlbumStockDTO(1L,"Nevermind", "Nirvana", 3, 10.99));

        when(mockAlbumRepository.getAlbumDTOById(1)).thenReturn(optionalAlbumDTO);

        AlbumStockDTO actualAlbum = service.getAlbumDTOById(1);


        assertAll(() -> {
            assertEquals("Nevermind", actualAlbum.getAlbumName());
            assertEquals("Nirvana", actualAlbum.getArtistName());
            assertEquals(10.99, actualAlbum.getPriceInPounds());
        });

        verify(mockAlbumRepository, times(1)).getAlbumDTOById(1);
    }

    @Test
    void getAlbumIdWhenGivenInvalidId() {
        Optional<AlbumStockDTO> optionalAlbumDTO = Optional.empty();

        when(mockAlbumRepository.getAlbumDTOById(1)).thenReturn(optionalAlbumDTO);

        AlbumStockDTO actualAlbum = service.getAlbumDTOById(1);

        assertNull(actualAlbum);
        verify(mockAlbumRepository, times(1)).getAlbumDTOById(1);
    }

    @Test
    void postAlbumFindsExistingAlbumAndUpdatesStock() {
        Optional<Album> optionalAlbumToPost = Optional.of(new Album(1L,"Nevermind", new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK ));
        Optional<Stock> optionalStock = Optional.of(new Stock(1L, 1L, 1099, 5));

        when(mockAlbumRepository.findAlbumByNameAndArtistName("Nevermind", "Nirvana")).thenReturn(optionalAlbumToPost);
        Album actualAlbum = optionalAlbumToPost.get();

        when(mockStockRepository.findAllByAlbumId()).thenReturn(optionalStock);
        Stock stock = optionalStock.get();
        stock.setNumberInStock(stock.getNumberInStock() + 1);
        when(mockStockRepository.save(stock)).thenReturn(stock);

        Album addedAlbum = service.addAlbum(actualAlbum);
        assertNotNull(addedAlbum);

        verify(mockAlbumRepository, times(1)).findAlbumByNameAndArtistName("Nevermind", "Nirvana");
        verify(mockStockRepository, times(1)).findAllByAlbumId();
        verify(mockStockRepository, times(1)).save(stock);
    }

    @Test
    void postAlbumDoesNotFindExistingAndUpdatesAlbumsAndStock() {
        Optional<Album> emptyOptional = Optional.empty();
        Album albumToAdd = new Album(1L,"Nevermind", new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK );
        Stock stockToAdd = new Stock(1L, 1L, 1099, 1);

        // Check if the album exists and receive an empty optional
        when(mockAlbumRepository.findAlbumByNameAndArtistName("Nirvana", "Nevermind")).thenReturn(emptyOptional);

        // Add the album to the albums table
        when(mockAlbumRepository.save(albumToAdd)).thenReturn(albumToAdd);

        // Add the stock to the stock table
        when(mockStockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        Album addedAlbum = service.addAlbum(albumToAdd);
        assertNotNull(addedAlbum);

        verify(mockAlbumRepository, times(1)).findAlbumByNameAndArtistName(anyString(), anyString());
        verify(mockAlbumRepository, times(1)).save(albumToAdd);
        verify(mockStockRepository, times(1)).save(stockToAdd);
    }

    @Test
    void postAlbumRejectInvalidAlbumAndReturnsNull() {
        Album firstAlbumToAdd = new Album(1L,"Nevermind", null, LocalDate.now(), Album.Genre.ROCK );
        Album secondAlbumToAdd = new Album(2L,null, new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK );

        Album firstResult = service.addAlbum(firstAlbumToAdd);
        Album secondResult = service.addAlbum(secondAlbumToAdd);

        assertNull(firstResult);
        assertNull(secondResult);
    }


}