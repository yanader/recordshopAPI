package com.northcoders.recordshopAPI.view;

public class UserInterface {
    static StockSystem stockSystem = new StockSystem();

    public static void run() {
        greetUser();
        while (true) {
            provideOptions();
            int choice = stockSystem.takeUserChoice();
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
                    // View all in-stock albums
                    break;
                case 4:
                    // view album by id
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

    private static void greetUser() {
        System.out.println("Welcome to the record store");
    }

    private static void quitProgramme() {System.out.println("Thanks for visiting our record store. Goodbye!");}

    private static void provideOptions() {
        System.out.print(stockSystem.provideOptions());
    }

    private static void viewAllAlbums() {
        stockSystem.viewAllAlbums();
    }

    private static void viewAllInStockAlbums() {
        stockSystem.viewAllInStockAlbums();
    }
}
