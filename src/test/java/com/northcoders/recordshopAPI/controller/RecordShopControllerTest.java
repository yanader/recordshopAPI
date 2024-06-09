package com.northcoders.recordshopAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.recordshopAPI.model.*;
import com.northcoders.recordshopAPI.service.RecordShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class RecordShopControllerTest {

    @Mock
    RecordShopServiceImpl mockService;

    @InjectMocks
    RecordShopController controller;

    @Autowired
    private MockMvc mockMvcController;

    private ObjectMapper mapper;

    @BeforeEach
    void setup() {
        mockMvcController = MockMvcBuilders.standaloneSetup(controller).build();
        mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);
    }

    @Test
    void getAllAlbums() throws Exception {
        List<AlbumDTO> albumDTOList = List.of(
                new AlbumDTO(new Album(1L, "Nevermind", "Nirvana", LocalDate.now(), Genre.ROCK )),
                new AlbumDTO(new Album(2L, "Owls", "Owls", LocalDate.now(), Genre.JAZZ )),
                new AlbumDTO(new Album(3L, "One More Time", "Britney Spears", LocalDate.now(), Genre.POP ))
        );

        when(mockService.getAllAlbums()).thenReturn(albumDTOList);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value("Nevermind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumName").value("Owls"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].albumName").value("One More Time"));

        verify(mockService, times(1)).getAllAlbums();
    }

    @Test
    void getALlInStockAlbums() throws Exception {
        List<AlbumStockDTO> inStockAlbums = List.of(
                new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2, 10.99 ),
                new AlbumStockDTO(2L,"Owls", "Owls", 4, 12.99),
                new AlbumStockDTO(3L, "One More Time", "Britney Spears", 1, 24.99)
        );

        when(mockService.getAllInStockAlbums()).thenReturn(inStockAlbums);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/instock"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value("Nevermind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].quantity").value("4"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[2].artistName").value("Britney Spears"));

        verify(mockService, times(1)).getAllInStockAlbums();
    }

    @Test
    void getAllInStockAlbumsReturnsMessageWhenNoStock() throws Exception {
        List<AlbumStockDTO> emptyList = List.of();

        when(mockService.getAllInStockAlbums()).thenReturn(emptyList);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/instock"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("There are no albums in stock"));

        verify(mockService, times(1)).getAllInStockAlbums();
    }

    @Test
    void getAlbumDTOByIdWithValidId() throws Exception{
        AlbumStockDTO album = new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2, 10.99 );

        when(mockService.getAlbumDTOById(1)).thenReturn(album);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value("Nevermind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistName").value("Nirvana"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value("2"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceInPounds").value("10.99"));

        verify(mockService, times(1)).getAlbumDTOById(1);
    }

    @Test
    void getAlbumDTOByIdWithInvalidId() throws Exception {

        when(mockService.getAlbumDTOById(1)).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordstore/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("No album with id 1 exists"));

        verify(mockService, times(1)).getAlbumDTOById(1);
    }

    @Test
    void postAlbumPostsSuccessfullyWithFullBody() throws Exception {
        SubmittedAlbumDTO albumToPost = new SubmittedAlbumDTO("Owls", "Owls", 1899, LocalDate.EPOCH, Genre.ROCK);
        Album addedAlbum = new Album(0L, "Owls", "Owls", LocalDate.now(), Genre.ROCK);

        when(mockService.addAlbum(albumToPost)).thenReturn(addedAlbum);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.post("/api/v1/recordstore/albums/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(albumToPost)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value("Owls"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistName").value("Owls"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.genre").value("ROCK"));
    }

    @Test
    void postAlbumPostsSuccessfullyWithMinimalBody() throws Exception {
        SubmittedAlbumDTO albumToPost = new SubmittedAlbumDTO("Owls", "Owls", null, null, null);
        Album addedAlbum = new Album(0L, "Owls", "Owls", null, null);

        when(mockService.addAlbum(albumToPost)).thenReturn(addedAlbum);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/recordstore/albums/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(albumToPost)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value("Owls"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistName").value("Owls"));
    }

    @Test
    void postAlbumRejectsAlbumWithMissingDetails() throws Exception {
        SubmittedAlbumDTO albumToPost = new SubmittedAlbumDTO("Owls", null, 1899, null, null);
        Album addedAlbum = new Album(0L, "Owls", "Owls", null, null);

        when(mockService.addAlbum(albumToPost)).thenReturn(null);
        when(mockService.invalidPostMessage()).thenCallRealMethod();


        this.mockMvcController.perform(
                        MockMvcRequestBuilders.post("/api/v1/recordstore/albums/add")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(albumToPost)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason(mockService.invalidPostMessage()));
    }

    @Test
    void putAlbumPutsAlbumSuccessfully() throws Exception {
        SubmittedAlbumDTO albumToPut = new SubmittedAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);

        AlbumStockDTO albumStockDTO = new AlbumStockDTO(1, "Nevermind", "Nirvana", 1, 10.99);

        when(mockService.putAlbum(albumToPut, 1)).thenReturn(albumStockDTO);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.put("/api/v1/recordstore/albums/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(mapper.writeValueAsString(albumToPut)))
                .andExpect(MockMvcResultMatchers.status().isCreated())
                .andExpect(MockMvcResultMatchers.jsonPath("$.albumName").value("Nevermind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.artistName").value("Nirvana"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.quantity").value("1"))
                .andExpect(MockMvcResultMatchers.jsonPath("$.priceInPounds").value("10.99"));
    }

    @Test
    void putAlbumFailsWithMissingId() throws Exception {
        SubmittedAlbumDTO albumToPut = new SubmittedAlbumDTO("Nevermind", "Nirvana", 1099, LocalDate.EPOCH, Genre.ROCK);

        when(mockService.putAlbum(albumToPut, 1)).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.put("/api/v1/recordstore/albums/1")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(mapper.writeValueAsString(albumToPut)))
                .andExpect(MockMvcResultMatchers.status().isBadRequest())
                .andExpect(MockMvcResultMatchers.status().reason("Error: Missing body or missing albumID"));
    }

    @Test
    void deleteAlbumDeletesAndConfirmsCorrectly() throws Exception{
        when(mockService.deleteById(1)).thenReturn(true);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.delete("/api/v1/recordstore/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isAccepted())
                .andExpect(MockMvcResultMatchers.content().string("Id 1 successfully deleted"));
    }

    @Test
    void deleteAlbumConfirmsDeleteFailed() throws Exception {
        when(mockService.deleteById(1)).thenReturn(false);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.delete("/api/v1/recordstore/albums/1"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("Deletion failed. No record at id 1"));
    }

    @Test
    void getAlbumsByArtistNameProvidesListOfAlbums() throws Exception {
        List<Album> albumList = List.of(
                new Album("Bleach", "Nirvana", LocalDate.EPOCH, Genre.ROCK),
                new Album("Nevermind", "Nirvana", LocalDate.EPOCH, Genre.ROCK)
        );

        when(mockService.getAllAlbumsByArtistName("Nirvana")).thenReturn(albumList);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/albums").param("artistname", "Nirvana"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value("bleach"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumName").value("nevermind"));
    }

    @Test
    void getAlbumsByArtistNameReportsNoAlbums() throws Exception {
        when(mockService.getAllAlbumsByArtistName("Nirvana")).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordstore/albums").param("artistname", "Nirvana"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("We have no albums by Nirvana"));
    }

    @Test
    void getAlbumsByReleaseYearSuppliesList() throws Exception {
        List<Album> albumList = List.of(
                new Album("Bleach", "Nirvana", LocalDate.now(), Genre.ROCK),
                new Album("Nevermind", "Nirvana", LocalDate.now(), Genre.ROCK)
        );

        when(mockService.getAlbumsByYear(2024)).thenReturn(albumList);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordstore/albums").param("year", "2024"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[0].albumName").value("bleach"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[1].albumName").value("nevermind"));
    }

    @Test
    void getAlbumsByReleaseYearReportsNoAlbums() throws Exception {
        when(mockService.getAlbumsByYear(1970)).thenReturn(null);

        this.mockMvcController.perform(
                        MockMvcRequestBuilders.get("/api/v1/recordstore/albums").param("year", "1970"))
                .andExpect(MockMvcResultMatchers.status().isNotFound())
                .andExpect(MockMvcResultMatchers.status().reason("We have no albums from 1970"));
    }




}