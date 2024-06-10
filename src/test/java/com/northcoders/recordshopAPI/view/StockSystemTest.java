package com.northcoders.recordshopAPI.view;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class StockSystemTest {

    @Mock
    Scanner scanner;

    @InjectMocks
    StockSystem stockSystem;

    @Test
    void takeUserChoice() {
        when(scanner.nextInt()).thenReturn(1, -1, 99, 2);

        int firstChoice = stockSystem.takeUserChoice();
        int secondChoice = stockSystem.takeUserChoice();

        assertEquals(1, firstChoice);
        assertEquals(2, secondChoice);

        verify(scanner, times(4)).nextInt();
    }

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
}