package com.northcoders.recordshopAPI.view;

import java.util.Scanner;

public class StockSystem {

    private Scanner scanner;
    private String[] choices;

    public StockSystem() {
        this.scanner = new Scanner(System.in);
        choices =
    }

    public int takeUserChoice() {
        int choice;
        while (true) {
            choice = scanner.nextInt();
            if (choice < 0 || choice > 8) {
                System.out.println("Please make a valid choice");
            }
        }
    }
}
