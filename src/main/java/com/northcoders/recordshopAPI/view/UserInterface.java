package com.northcoders.recordshopAPI.view;

public class UserInterface {

    public static void run() {
        StockSystem stockSystem = new StockSystem();
        greetUser();
        while (true) {

            provideOptions();
            int choice = stockSystem.takeUserChoice();
            // Take choice of actions

            // Switch between choice
              // Inc quit

            //Loop
        }

            // Say goodbye!
    }

    private static void greetUser() {
        System.out.println("Welcome to the record store");
    }

    private static void provideOptions() {
        System.out.print("""
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
                """);
    }
}
