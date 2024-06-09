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
        SubmittedAlbumDTO submittedAlbumDTO = new SubmittedAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.now(), Genre.ROCK);
        Optional<Album> optionalAlbumToPost = Optional.of(new Album(0L,"Nevermind", "Nirvana", LocalDate.now(), Genre.ROCK ));
        Optional<Stock> optionalStock = Optional.of(new Stock(0L, 0L, 1099, 5));

        when(mockAlbumRepository.findByAlbumNameAndArtistName("Nevermind", "Nirvana")).thenReturn(optionalAlbumToPost);
        // Album actualAlbum = optionalAlbumToPost.get();

        when(mockStockRepository.findAllByAlbumId(0L)).thenReturn(optionalStock);
        Stock stock = optionalStock.get();



        Album addedAlbum = service.addAlbum(submittedAlbumDTO);
        assertNotNull(addedAlbum);
        assertEquals(6, stock.getNumberInStock());

        verify(mockAlbumRepository, times(1)).findByAlbumNameAndArtistName("Nevermind", "Nirvana");
        verify(mockStockRepository, times(1)).findAllByAlbumId(0L);
    }

    @Test
    void postAlbumDoesNotFindExistingAndUpdatesAlbumsAndStock() {
        Optional<Album> emptyOptional = Optional.empty();
        SubmittedAlbumDTO submittedAlbumDTO = new SubmittedAlbumDTO("nevermind", "nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);
        Album albumToAdd = new Album(0L,"nevermind", "nirvana", LocalDate.EPOCH, Genre.ROCK );
        Stock stockToAdd = new Stock(0L, 0L, 1099, 1);

        // Dictate empty optional from checking if album exists
        when(mockAlbumRepository.findByAlbumNameAndArtistName("nevermind", "nirvana")).thenReturn(emptyOptional);

        // Dictate album is returned when saved
        when(mockAlbumRepository.save(albumToAdd)).thenReturn(albumToAdd);

        // Dictate returning Stock item when we add it (automatic without calculation because album wasn't found
        when(mockStockRepository.save(stockToAdd)).thenReturn(stockToAdd);

        Album addedAlbum = service.addAlbum(submittedAlbumDTO);
        assertNotNull(addedAlbum);

        assertEquals(albumToAdd, addedAlbum);

        verify(mockAlbumRepository, times(1)).findByAlbumNameAndArtistName("nevermind", "nirvana");
        verify(mockAlbumRepository, times(1)).save(albumToAdd);
        verify(mockStockRepository, times(1)).save(stockToAdd);
    }

    @Test
    void postAlbumRejectInvalidAlbumAndReturnsNull() {
        SubmittedAlbumDTO firstPostAlbum = new SubmittedAlbumDTO(null, "Nirvana", 1099, LocalDate.now(), Genre.ROCK);
        SubmittedAlbumDTO secondPostAlbum = new SubmittedAlbumDTO("Nevermind", null, 1099, LocalDate.now(), Genre.ROCK);

        Album firstResult = service.addAlbum(firstPostAlbum);
        Album secondResult = service.addAlbum(secondPostAlbum);

        assertNull(firstResult);
        assertNull(secondResult);
    }

    @Test
    void putAlbumUpdatesAlbumSuccessfully() {
        SubmittedAlbumDTO submittedAlbumDTO = new SubmittedAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);
        int idToPutAt = 1;
        Album albumToAdd = new Album(1L,"Nevermind", "Nirvana", LocalDate.EPOCH, Genre.ROCK );
        Optional<Stock> optionalStock = Optional.of(new Stock(0, 1299, 1));

        when(mockAlbumRepository.existsById(1)).thenReturn(true);
        when(mockAlbumRepository.save(albumToAdd)).thenReturn(albumToAdd);

        when(mockStockRepository.findAllByAlbumId(idToPutAt)).thenReturn(optionalStock);

        AlbumStockDTO addedAlbumStockDTO = service.putAlbum(submittedAlbumDTO, idToPutAt);

        assertNotNull(addedAlbumStockDTO);
        assertEquals(addedAlbumStockDTO.getAlbumName(), submittedAlbumDTO.getAlbumName());
        assertEquals(addedAlbumStockDTO.getArtistName(), submittedAlbumDTO.getArtistName());


        verify(mockAlbumRepository, times(1)).existsById(1);
        verify(mockAlbumRepository, times(1)).save(albumToAdd);
    }

    @Test
    void putAlbumFailsWithIncompleteBody() {
        SubmittedAlbumDTO firstAlbumDTO = new SubmittedAlbumDTO(null, "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);
        SubmittedAlbumDTO secondAlbumDTO = new SubmittedAlbumDTO("Nevermind", null, 1099, LocalDate.EPOCH, Genre.ROCK);
        SubmittedAlbumDTO thirdAlbumDTO = new SubmittedAlbumDTO("Nevermind", "Nirvana", null, LocalDate.EPOCH, Genre.ROCK);

        AlbumStockDTO firstAlbumStockDTO = service.putAlbum(firstAlbumDTO, 1);
        AlbumStockDTO secondAlbumStockDTO = service.putAlbum(secondAlbumDTO, 1);
        AlbumStockDTO thirdAlbumStockDTO = service.putAlbum(thirdAlbumDTO, 1);

        assertNull(firstAlbumStockDTO);
        assertNull(secondAlbumStockDTO);
        assertNull(thirdAlbumStockDTO);
    }

    @Test
    void putAlbumFailsWithInvalidAlbumId() {
        SubmittedAlbumDTO firstAlbumDTO = new SubmittedAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);
        when(mockAlbumRepository.existsById(0)).thenReturn(false);

        AlbumStockDTO firstAlbumStockDTO = service.putAlbum(firstAlbumDTO, 0);

        assertNull(firstAlbumStockDTO);
        verify(mockAlbumRepository, times(1)).existsById(0);
    }

    @Test
    void deleteAlbumByIdIsSuccessful() {

        when(mockAlbumRepository.existsById(1)).thenReturn(true);

        boolean result = service.deleteById(1);

        assertTrue(result);

        verify(mockAlbumRepository, times(1)).existsById(1);
        verify(mockAlbumRepository, times(1)).deleteById(1);
        verify(mockStockRepository, times(1)).deleteByAlbumId(1);
    }

    @Test
    void deleteAlbumByIdFailsWithInvalidId() {

        when(mockAlbumRepository.existsById(1)).thenReturn(false);

        boolean result = service.deleteById(1);

        assertFalse(result);

        verify(mockAlbumRepository, times(1)).existsById(1);
        verify(mockAlbumRepository, times(0)).deleteById(1);
        verify(mockStockRepository, times(0)).deleteByAlbumId(1);
    }

    @Test
    void getAllAlbumsFilteredByArtist() {
        List<Album> albumList = List.of(
                new Album("bleach", "nirvana", LocalDate.EPOCH, Genre.ROCK),
                new Album("nevermind", "nirvana", LocalDate.EPOCH, Genre.ROCK)
        );

        when(mockAlbumRepository.findByArtistName("nirvana")).thenReturn(albumList);

        List<Album> resultsList = service.getAllAlbumsByArtistName("nirvana");

        assertEquals(2, resultsList.size());
        assertEquals("bleach", resultsList.get(0).getAlbumName());
        assertEquals("nevermind", resultsList.get(1).getAlbumName());
        verify(mockAlbumRepository,times(1)).findByArtistName("nirvana");
    }

    @Test
    void getAllAlbumsFilterByArtistNameReturnsEmptyList() {
        List<Album> emptyList = List.of();

        when(mockAlbumRepository.findByArtistName("nirvana")).thenReturn(emptyList);

        List<Album> resultsList = service.getAllAlbumsByArtistName("Nirvana");

        assertNull(resultsList);
        verify(mockAlbumRepository,times(1)).findByArtistName("nirvana");
    }

    @Test
    void getAllAlbumsWithSpecifiedReleaseYearr() {
        List<Album> albumList = List.of(
                new Album("bleach", "nirvana", LocalDate.EPOCH, Genre.ROCK),
                new Album("nevermind", "nirvana", LocalDate.EPOCH, Genre.ROCK)
        );
        int year = 1970;
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        when(mockAlbumRepository.findByReleaseDateBetween(start, end)).thenReturn(albumList);

        List<Album> resultsList = service.getAlbumsByYear(1970);

        assertEquals(2, resultsList.size());
        assertEquals("bleach", resultsList.get(0).getAlbumName());
        assertEquals("nevermind", resultsList.get(1).getAlbumName());
        verify(mockAlbumRepository,times(1)).findByReleaseDateBetween(start, end);
    }

    @Test
    void getAllAlbumsFilterByReleaseYearReturnsNull() {
        List<Album> emptyList = List.of();

        int year = 1970;
        LocalDate start = LocalDate.of(year, 1, 1);
        LocalDate end = LocalDate.of(year, 12, 31);

        when(mockAlbumRepository.findByReleaseDateBetween(start, end)).thenReturn(emptyList);

        List<Album> resultsList = service.getAlbumsByYear(1970);

        assertNull(resultsList);
        verify(mockAlbumRepository,times(1)).findByReleaseDateBetween(start, end);
    }

    @Test
    void getAllAlbumsByGenreReturnsList() {
        List<Album> albumList = List.of(
                new Album("bleach", "nirvana", LocalDate.EPOCH, Genre.ROCK),
                new Album("nevermind", "nirvana", LocalDate.EPOCH, Genre.ROCK)
        );

        when(mockAlbumRepository.findByGenre(Genre.ROCK)).thenReturn(albumList);

        List<Album> resultsList = service.getAlbumsByGenre("ROCK");

        assertEquals(2, resultsList.size());
        assertEquals("bleach", resultsList.get(0).getAlbumName());
        assertEquals("nevermind", resultsList.get(1).getAlbumName());
        verify(mockAlbumRepository,times(1)).findByGenre(Genre.ROCK);
    }

    @Test
    void getAllAlbumsByGenreReturnsNull() {
        List<Album> emptyList = List.of();

        when(mockAlbumRepository.findByGenre(Genre.ROCK)).thenReturn(emptyList);

        List<Album> resultsList = service.getAlbumsByGenre("ROCK");

        assertNull(resultsList);
        verify(mockAlbumRepository,times(1)).findByGenre(Genre.ROCK);
    }

    @Test
    void getAlbumByAlbumNameReturnsAlbum() {
        Album album = new Album("bleach", "nirvana", LocalDate.EPOCH, Genre.ROCK);
        Stock stock = new Stock(1L, 0L, 1050, 2);


        when(mockAlbumRepository.findByAlbumName("bleach")).thenReturn(Optional.of(album));
        when(mockStockRepository.findAllByAlbumId(0L)).thenReturn(Optional.of(stock));

        AlbumStockDTO albumStockDTO = service.getAlbumDetailsByAlbumName("bleach");

        assertNotNull(albumStockDTO);
        assertEquals(2, albumStockDTO.getQuantity());
        assertEquals(10.50, albumStockDTO.getPriceInPounds());

        verify(mockAlbumRepository, times(1)).findByAlbumName("bleach");
        verify(mockStockRepository, times(1)).findAllByAlbumId(0L);

    }

    @Test
    void getAlbumByAlbumNameReturnsNull() {
        when(mockAlbumRepository.findByAlbumName("bleach")).thenReturn(Optional.empty());

        AlbumStockDTO albumStockDTO = service.getAlbumDetailsByAlbumName("bleach");

        assertNull(albumStockDTO);
        verify(mockAlbumRepository, times(1)).findByAlbumName("bleach");
        verify(mockStockRepository, times(0)).findAllByAlbumId(anyLong());
    }

    @Test
    void patchUpdateAlbum() {
        Album albumToUpdate = new Album("bleach", "nirvana", LocalDate.EPOCH, Genre.ROCK);
        Stock updateStock = new Stock(1L, 1L, 1099, 1);
        UpdateAlbumDTO updates = new UpdateAlbumDTO("Owls", "Owls", LocalDate.of(1999, 1, 1), Genre.POP, 500, 100);
        Album albumUpdates = new Album("Owls", "Owls", LocalDate.of(1999, 1, 1), Genre.POP);

        when(mockAlbumRepository.existsById(anyInt())).thenReturn(true);
        when(mockAlbumRepository.findById(anyInt())).thenReturn(Optional.of(albumToUpdate));
        when(mockStockRepository.findAllByAlbumId(anyLong())).thenReturn(Optional.of(updateStock));
        when(mockAlbumRepository.save(any(Album.class))).thenReturn(albumUpdates);

        Album updatedAlbum = service.updateAlbumDetails(1L, updates);

        assertEquals("owls", updatedAlbum.getAlbumName());
        assertEquals("owls", updatedAlbum.getArtistName());
        assertEquals(Genre.POP, updatedAlbum.getGenre());

        verify(mockAlbumRepository, times(1)).findById(anyInt());
        verify(mockStockRepository, times(1)).save(updateStock);
        verify(mockAlbumRepository, times(1)).save(albumToUpdate);
    }

    @Test
    void patchUpdateAlbumRejectsInvalidId() {
        when(mockAlbumRepository.existsById(anyInt())).thenReturn(false);
        UpdateAlbumDTO updates = new UpdateAlbumDTO("Owls", "Owls", LocalDate.of(1999, 1, 1), Genre.POP, 500, 100);

        Album updatedAlbum = service.updateAlbumDetails(1, updates);

        assertNull(updatedAlbum);

        verify(mockAlbumRepository, times(1)).existsById(anyInt());
        verify(mockAlbumRepository, times(0)).findById(anyInt());
        verify(mockStockRepository, times(0)).findAllByAlbumId(anyInt());
        verify(mockStockRepository, times(0)).save(any(Stock.class));
        verify(mockAlbumRepository, times(0)).save(any(Album.class));

    }


}