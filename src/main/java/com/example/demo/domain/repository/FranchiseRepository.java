package com.example.demo.repository;

import com.example.demo.domain.model.Franchise;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface FranchiseRepository
  extends ReactiveMongoRepository<Franchise, String> {}
