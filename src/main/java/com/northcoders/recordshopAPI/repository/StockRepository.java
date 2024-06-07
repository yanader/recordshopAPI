package com.northcoders.recordshopAPI.repository;

import com.northcoders.recordshopAPI.model.Stock;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockRepository extends CrudRepository<Stock, Integer> {
}
