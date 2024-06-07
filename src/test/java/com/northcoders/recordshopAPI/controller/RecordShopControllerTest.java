package com.northcoders.recordshopAPI.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.northcoders.recordshopAPI.model.Album;
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
}