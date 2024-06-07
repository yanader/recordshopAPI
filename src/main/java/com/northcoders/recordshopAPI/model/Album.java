package com.northcoders.recordshopAPI.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Album {

    @Id
    @GeneratedValue
    private long albumId;

    @NotNull
    private String name;

    @NotNull
    @ManyToOne
    @JoinColumn(name = "artistId")
    private Artist artist;

    private int yearOfRelease;

    private Genre genre;


}
