package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ArtistRepository extends CrudRepository<Artist, Integer> {

    Optional<Artist> findByName(String name);
}
