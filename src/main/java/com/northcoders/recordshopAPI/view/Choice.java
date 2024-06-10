package com.northcoders.recordshopAPI.view;

public enum Choice {
    QUIT(0, "Quit"),
    VIEW(1, "View all albums"),
    FILTER(2, "View all albums with filter"),
    IN_STOCK(3, "View all in-stock albums"),
    ID(4, "View album by id"),
    ADD(5, "Add an album"),
    UPDATE_WHOLE(6, "Update an album (whole)"),
    UPDATE_PARTIAL(7, "Update an album (partial)"),
    DELETE(8, "Delete an album");

        private final int number;
        private final String text;

    Choice(int number, String text) {
        this.number = number;
        this.text = text;
    }

    public int getNumber() {
        return number;
    }

    public String getText() {
        return text;
    }
}
