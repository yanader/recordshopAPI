package com.northcoders.recordshopAPI.view;

import com.northcoders.recordshopAPI.model.AlbumStockDTO;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Scanner;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserInterfaceTest {

    @Mock
    Scanner mockScanner;

    @InjectMocks
    UserInterface userInterface;

    @Test
    void takeUserChoice() {
        when(mockScanner.nextInt()).thenReturn(1, -1, 99, 2);

        int firstChoice = userInterface.takeUserChoice();
        int secondChoice = userInterface.takeUserChoice();

        assertEquals(1, firstChoice);
        assertEquals(2, secondChoice);

        verify(mockScanner, times(4)).nextInt();
    }

}