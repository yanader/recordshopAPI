package com.northcoders.recordshopAPI.view;

public class UserInterface {
    static StockSystem stockSystem = new StockSystem();

    public static void run() {

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
        System.out.print(stockSystem.provideOptions());
    }
}
