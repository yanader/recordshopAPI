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
}