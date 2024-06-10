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
            if (choice < 0 || choice >= Choice.values().length) {
                System.out.println("Please make a valid choice");
                continue;
            }
            return choice;
        }
    }

    public String provideOptions() {
        StringBuilder sb = new StringBuilder();
        sb.append("What would you like to do today?\n");
        for (int i = 1; i < Choice.values().length; i++) {
            sb.append("\t").append(Choice.values()[i].getNumber()).append(". ").append(Choice.values()[i].getText()).append("\n");
        }
        sb.append("\t").append(Choice.values()[0].getNumber()).append(". ").append(Choice.values()[0].getText()).append("\n");
        return sb.toString();
    }
}
