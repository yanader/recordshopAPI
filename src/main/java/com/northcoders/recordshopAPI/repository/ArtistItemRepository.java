package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Artist;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ArtistItemRepository extends CrudRepository<Artist, Integer> {
}
