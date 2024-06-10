package com.northcoders.recordshopAPI.view;

import java.io.IOException;
import java.util.InputMismatchException;
import java.util.Scanner;

public class UserInterface {
    StockSystem stockSystem = new StockSystem();
    Scanner scanner = new Scanner(System.in);

    public void run() {
        greetUser();
        while (true) {
            provideOptions();
            int choice = takeUserChoice();
            // Take choice of actions
            if (choice == 0) {
                quitProgramme();
                break;
            }
            switch (choice) {
                case 1:
                    // View All albums
                    viewAllAlbums();
                    break;
                case 2:
                    // View all albums with filter
                    break;
                case 3:
                    viewAllInStockAlbums();
                    break;
                case 4:
                    viewAlbumDetailsById();
                    break;
                case 5:
                    // add an album
                    break;
                case 6:
                    // Update an album - whole
                    break;
                case 7:
                    // Update an album - partial
                    break;
                case 8:
                    // delete an album
                    break;
            }
        }
    }

    private void greetUser() {
        System.out.println("Welcome to the record store");
    }

    private void quitProgramme() {System.out.println("Thanks for visiting our record store. Goodbye!");}

    private void provideOptions() {
        System.out.print(stockSystem.provideOptions());
    }

    public int takeUserChoice() {
        int choice;
        while (true) {
            choice = scanner.nextInt();
            if (choice < 0 || choice >= Choice.values().length) {
                System.out.println("Please make a valid choice");
                continue;
            }
            return choice;
        }
    }

    private void viewAllAlbums() {
        stockSystem.viewAllAlbums();
    }

    private void viewAllInStockAlbums() {
        stockSystem.viewAllInStockAlbums();
    }
    private void viewAlbumDetailsById() {
        long id = getIdFromUser();
        stockSystem.viewAlbumById(id);
    }

    private long getIdFromUser() {
        while(true) {
            System.out.println("Id of album to view:");
            try {
                return scanner.nextLong();
            } catch (InputMismatchException e) {
                System.out.println("Not a valid Id format");
            }
        }
    }
}
