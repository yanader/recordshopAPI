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
    private long id;

    @NotNull
    private String name;

    private List<Record> discography;
}
