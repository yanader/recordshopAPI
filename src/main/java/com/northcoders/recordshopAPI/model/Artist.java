package com.northcoders.recordshopAPI.model;

import jakarta.persistence.*;
import lombok.*;
import jakarta.validation.constraints.NotNull;

import java.util.List;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Artist {

    @Id
    @GeneratedValue
    private long artistId;

    @NotNull
    private String name;

    @ElementCollection
    @OneToMany(mappedBy="artist")
    private List<Album> discography;
}
