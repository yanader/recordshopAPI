package com.northcoders.recordshopAPI.repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface StockItemRepository extends CrudRepository<StockItemRepository, Integer> {
}