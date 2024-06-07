package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {

    Optional<Stock> findAllByAlbumId(long id);
}
