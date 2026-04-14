package com.example.demo.application.service;

import com.example.demo.domain.model.*;
import com.example.demo.repository.FranchiseRepository;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * Application Service for Franchise Management.
 * Implements a Reactive Programming paradigm to ensure high throughput
 * and system scalability, as required by the technical assessment.
 */
@Service
@RequiredArgsConstructor
public class FranchiseService {

  private final FranchiseRepository repository;

  /**
   * Retrieves all registered franchises.
   * @return A Flux containing all Franchise entities (Non-blocking 0..N stream).
   */
  public Flux<Franchise> getAllFranchises() {
    return repository.findAll();
  }

  /**
   * Persists a new franchise into the database.
   * @param franchise The franchise object to be saved.
   * @return A Mono containing the persisted Franchise entity.
   */
  public Mono<Franchise> createFranchise(Franchise franchise) {
    return repository.save(franchise);
  }

  /**
   * Adds a branch to an existing franchise.
   * Uses flatMap to chain reactive operations without blocking the execution thread.
   * @param franchiseId The unique identifier of the target franchise.
   * @param branch The branch object to be added.
   * @return A Mono with the updated Franchise.
   */
  public Mono<Franchise> addBranch(String franchiseId, Branch branch) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() == null) f.setBranches(new ArrayList<>());
        f.getBranches().add(branch);
        return repository.save(f);
      });
  }

  /**
   * Adds a product to a specific branch within a franchise.
   * Implements functional filtering to find the correct branch before adding the product.
   */
  public Mono<Franchise> addProductToBranch(
    String franchiseId,
    String branchId,
    Product product
  ) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() != null) {
          f
            .getBranches()
            .stream()
            .filter(b -> b.getId().equals(branchId))
            .findFirst()
            .ifPresent(b -> {
              if (b.getProducts() == null) b.setProducts(new ArrayList<>());
              b.getProducts().add(product);
            });
        }
        return repository.save(f);
      });
  }

  /**
   * Deletes a product from a specific branch.
   * Ensures data integrity by removing the item from the nested collection.
   */
  public Mono<Franchise> deleteProduct(
    String franchiseId,
    String branchId,
    String productId
  ) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() != null) {
          f
            .getBranches()
            .stream()
            .filter(b -> b.getId().equals(branchId))
            .findFirst()
            .ifPresent(b -> {
              if (b.getProducts() != null) {
                b.getProducts().removeIf(p -> p.getId().equals(productId));
              }
            });
        }
        return repository.save(f);
      });
  }

  /**
   * Updates the stock of a specific product.
   * Demonstrates the use of functional streams for deep object graph updates.
   */
  public Mono<Franchise> updateProductStock(
    String franchiseId,
    String branchId,
    String productId,
    int newStock
  ) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() != null) {
          f
            .getBranches()
            .stream()
            .filter(b -> b.getId().equals(branchId))
            .flatMap(b ->
              b.getProducts() != null
                ? b.getProducts().stream()
                : java.util.stream.Stream.empty()
            )
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .ifPresent(p -> p.setStock(newStock));
        }
        return repository.save(f);
      });
  }

  /**
   * Core logic to retrieve the product with the highest stock per branch for a franchise.
   * This method fulfills the advanced business requirement for data aggregation.
   * @param franchiseId Target franchise ID.
   * @return A Mono list of anonymous objects containing Branch name, Product name, and Stock level.
   */
  public Mono<List<Object>> getTopStockProducts(String franchiseId) {
    return repository
      .findById(franchiseId)
      .map(f -> {
        if (f.getBranches() == null) return new ArrayList<>();
        return f
          .getBranches()
          .stream()
          .map(b -> {
            if (
              b.getProducts() == null || b.getProducts().isEmpty()
            ) return java.util.Optional.empty();

            return b
              .getProducts()
              .stream()
              .max(Comparator.comparingInt(Product::getStock))
              .map(p ->
                new Object() {
                  public final String branch = b.getName();
                  public final String product = p.getName();
                  public final int stock = p.getStock();
                }
              );
          })
          .filter(java.util.Optional::isPresent)
          .map(java.util.Optional::get)
          .collect(Collectors.toList());
      });
  }

  /**
   * Updates the franchise name.
   */
  public Mono<Franchise> updateFranchiseName(String id, String newName) {
    return repository
      .findById(id)
      .flatMap(f -> {
        f.setName(newName);
        return repository.save(f);
      });
  }

  /**
   * Updates the name of a specific branch.
   */
  public Mono<Franchise> updateBranchName(
    String franchiseId,
    String branchId,
    String newName
  ) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() != null) {
          f
            .getBranches()
            .stream()
            .filter(b -> b.getId().equals(branchId))
            .findFirst()
            .ifPresent(b -> b.setName(newName));
        }
        return repository.save(f);
      });
  }

  /**
   * Updates the name of a specific product using a non-blocking approach.
   */
  public Mono<Franchise> updateProductName(
    String franchiseId,
    String branchId,
    String productId,
    String newName
  ) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() != null) {
          f
            .getBranches()
            .stream()
            .filter(b -> b.getId().equals(branchId))
            .flatMap(b ->
              b.getProducts() != null
                ? b.getProducts().stream()
                : java.util.stream.Stream.empty()
            )
            .filter(p -> p.getId().equals(productId))
            .findFirst()
            .ifPresent(p -> p.setName(newName));
        }
        return repository.save(f);
      });
  }
}
