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

@Service
@RequiredArgsConstructor
public class FranchiseService {

  private final FranchiseRepository repository;

  public Flux<Franchise> getAllFranchises() {
    return repository.findAll();
  }

  public Mono<Franchise> createFranchise(Franchise franchise) {
    return repository.save(franchise);
  }

  public Mono<Franchise> addBranch(String franchiseId, Branch branch) {
    return repository
      .findById(franchiseId)
      .flatMap(f -> {
        if (f.getBranches() == null) f.setBranches(new ArrayList<>());
        f.getBranches().add(branch);
        return repository.save(f);
      });
  }

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
                  public final String sucursal = b.getName();
                  public final String producto = p.getName();
                  public final int stock = p.getStock();
                }
              );
          })
          .filter(java.util.Optional::isPresent)
          .map(java.util.Optional::get)
          .collect(Collectors.toList());
      });
  }

  public Mono<Franchise> updateFranchiseName(String id, String newName) {
    return repository
      .findById(id)
      .flatMap(f -> {
        f.setName(newName);
        return repository.save(f);
      });
  }

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
