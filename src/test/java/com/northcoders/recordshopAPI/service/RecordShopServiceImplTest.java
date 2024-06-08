package com.northcoders.recordshopAPI.service;

import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.repository.AlbumRepository;
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
    StockRepository mockStockRepository;

    @InjectMocks
    RecordShopServiceImpl service;


    @Test
    void getAllAlbums() {
        List<Album> albumList = List.of(
                new Album("Nevermind", "Nirvana", LocalDate.now(), Genre.ROCK ),
                new Album("Owls", "Owls", LocalDate.now(), Genre.JAZZ ),
                new Album("One More Time", "Britney Spears", LocalDate.now(), Genre.POP )
        );

        when(mockAlbumRepository.findAll()).thenReturn(albumList);

        List<AlbumDTO> resultList = service.getAllAlbums();

        assertEquals(3, resultList.size());
        assertEquals("Nirvana", resultList.get(0).getArtistName());
        assertEquals("Owls", resultList.get(1).getArtistName());
        assertEquals("Britney Spears", resultList.get(2).getArtistName());
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
    void getAlbumByIdWhenGivenValidId() {
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
        PostAlbumDTO postAlbumDTO = new PostAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.now(), Genre.ROCK);
        Optional<Album> optionalAlbumToPost = Optional.of(new Album(0L,"Nevermind", "Nirvana", LocalDate.now(), Genre.ROCK ));
        Optional<Stock> optionalStock = Optional.of(new Stock(0L, 0L, 1099, 5));

        when(mockAlbumRepository.findByAlbumNameAndArtistName("Nevermind", "Nirvana")).thenReturn(optionalAlbumToPost);
        // Album actualAlbum = optionalAlbumToPost.get();

        when(mockStockRepository.findAllByAlbumId(0L)).thenReturn(optionalStock);
        Stock stock = optionalStock.get();



        Album addedAlbum = service.addAlbum(postAlbumDTO);
        assertNotNull(addedAlbum);
        assertEquals(6, stock.getNumberInStock());

        verify(mockAlbumRepository, times(1)).findByAlbumNameAndArtistName("Nevermind", "Nirvana");
        verify(mockStockRepository, times(1)).findAllByAlbumId(0L);
    }

    @Test
    void postAlbumDoesNotFindExistingAndUpdatesAlbumsAndStock() {
        Optional<Album> emptyOptional = Optional.empty();
        PostAlbumDTO postAlbumDTO = new PostAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);
        Album albumToAdd = new Album(0L,"Nevermind", "Nirvana", LocalDate.EPOCH, Genre.ROCK );
        Stock stockToAdd = new Stock(0L, 0L, 1099, 1);

        // Dictate empty optional from checking if album exists
        when(mockAlbumRepository.findByAlbumNameAndArtistName("Nevermind", "Nirvana")).thenReturn(emptyOptional);

        // Dictate album is returned when saved
        when(mockAlbumRepository.save(albumToAdd)).thenReturn(albumToAdd);

        // Dictate returning Stock item when we add it (automatic without calculation because album wasn't found
        when(mockStockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        Album addedAlbum = service.addAlbum(postAlbumDTO);
        assertNotNull(addedAlbum);

        assertEquals(albumToAdd, addedAlbum);

        verify(mockAlbumRepository, times(1)).findByAlbumNameAndArtistName("Nevermind", "Nirvana");
        verify(mockAlbumRepository, times(1)).save(albumToAdd);
        verify(mockStockRepository, times(1)).save(stockToAdd);
    }

    @Test
    void postAlbumRejectInvalidAlbumAndReturnsNull() {
        PostAlbumDTO firstPostAlbum = new PostAlbumDTO(null, "Nirvana", 1099, LocalDate.now(), Genre.ROCK);
        PostAlbumDTO secondPostAlbum = new PostAlbumDTO("Nevermind", null, 1099, LocalDate.now(), Genre.ROCK);

        Album firstResult = service.addAlbum(firstPostAlbum);
        Album secondResult = service.addAlbum(secondPostAlbum);

        assertNull(firstResult);
        assertNull(secondResult);
    }


}