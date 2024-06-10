package com.northcoders.recordshopAPI.view;

import java.util.Scanner;

public class StockSystem {

    private Scanner scanner;

    public StockSystem() {
        this.scanner = new Scanner(System.in);
    }

    public int takeUserChoice() {
        int choice;
        while (true) {
            choice = scanner.nextInt();
            if (choice < 0 || choice > Choice.values().length - 1) {
                System.out.println("Please make a valid choice");
                continue;
            }
            return choice;
        }
    }
}
