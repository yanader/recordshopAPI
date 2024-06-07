package com.northcoders.recordshopAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.recordshopAPI.model.Album;
import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import com.northcoders.recordshopAPI.model.Artist;
import com.northcoders.recordshopAPI.service.RecordShopServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
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
//        mapper.registerModule(new JavaTimeModule());
    }

    @Test
    void getAllAlbums() throws Exception {
        List<Album> albumList = List.of(
                new Album(1L, "Nevermind", new Artist("Nirvana"), LocalDate.now(), Album.Genre.ROCK ),
                new Album(2L, "Owls", new Artist("Owls"), LocalDate.now(), Album.Genre.JAZZ ),
                new Album(3L, "One More Time", new Artist("Britney Spears"), LocalDate.now(), Album.Genre.POP )
        );

        when(mockService.getAllAlbums()).thenReturn(albumList);

        this.mockMvcController.perform(
                MockMvcRequestBuilders.get("/api/v1/recordstore/albums"))
                .andExpect(MockMvcResultMatchers.status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.albumId == 1)].name").value("Nevermind"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.albumId == 2)].name").value("Owls"))
                .andExpect(MockMvcResultMatchers.jsonPath("$[?(@.albumId == 3)].name").value("One More Time"));

        verify(mockService, times(1)).getAllAlbums();
    }

    @Test
    void getALlInStockAlbums() throws Exception {
        List<AlbumStockDTO> inStockAlbums = List.of(
                new AlbumStockDTO(1L, "Nevermind", "Nirvana", 2 ),
                new AlbumStockDTO(2L,"Owls", "Owls", 4),
                new AlbumStockDTO(3L, "One More Time", "Britney Spears", 1)
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
}