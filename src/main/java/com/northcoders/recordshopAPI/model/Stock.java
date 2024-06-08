package com.northcoders.recordshopAPI.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long stockId;

    @NotNull
    private long albumId;

    @NotNull
    private int priceInPence;

    @NotNull
    private int numberInStock;

    public Stock(long albumId, int priceInPence, int numberInStock) {
        this.albumId = albumId;
        this.priceInPence = priceInPence;
        this.numberInStock = numberInStock;
    }
}
