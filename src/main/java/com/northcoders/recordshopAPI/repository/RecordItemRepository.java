package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Album;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RecordItemRepository extends CrudRepository<Album, Integer> {
}
