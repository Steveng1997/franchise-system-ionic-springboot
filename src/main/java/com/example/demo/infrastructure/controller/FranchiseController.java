package com.example.demo.infrastructure.controller;

import com.example.demo.application.service.FranchiseService;
import com.example.demo.domain.model.*;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

/**
 * REST Controller providing reactive endpoints for Franchise management.
 * Adheres to RESTful principles and non-blocking I/O.
 */
@RestController
@RequestMapping("/api/franchises")
@RequiredArgsConstructor
public class FranchiseController {

  private final FranchiseService service;

  /**
   * Retrieves all franchises.
   */
  @GetMapping
  public Flux<Franchise> getAll() {
    return service.getAllFranchises();
  }

  /**
   * Creates a new franchise aggregate.
   */
  @PostMapping
  public Mono<Franchise> create(@RequestBody Franchise franchise) {
    return service.createFranchise(franchise);
  }

  /**
   * Adds a new branch to a specific franchise.
   */
  @PostMapping("/{id}/branches")
  public Mono<Franchise> addBranch(
    @PathVariable String id,
    @RequestBody Branch branch
  ) {
    return service.addBranch(id, branch);
  }

  /**
   * Adds a product to a specific branch.
   */
  @PostMapping("/{id}/branches/{branchId}/products")
  public Mono<Franchise> addProduct(
    @PathVariable String id,
    @PathVariable String branchId,
    @RequestBody Product product
  ) {
    return service.addProductToBranch(id, branchId, product);
  }

  /**
   * Deletes a product from a specific branch.
   */
  @DeleteMapping("/{id}/branches/{branchId}/products/{productId}")
  public Mono<Franchise> deleteProduct(
    @PathVariable String id,
    @PathVariable String branchId,
    @PathVariable String productId
  ) {
    return service.deleteProduct(id, branchId, productId);
  }

  /**
   * Updates the stock level of a specific product via Patch.
   */
  @PatchMapping("/{id}/branches/{branchId}/products/{productId}/stock")
  public Mono<Franchise> updateStock(
    @PathVariable String id,
    @PathVariable String branchId,
    @PathVariable String productId,
    @RequestParam int stock
  ) {
    return service.updateProductStock(id, branchId, productId, stock);
  }

  /**
   * Retrieves the products with the maximum stock per branch for a given franchise.
   */
  @GetMapping("/{id}/max-stock-products")
  public Mono<List<Object>> getMaxStock(@PathVariable String id) {
    return service.getTopStockProducts(id);
  }

  /**
   * Updates the name of the franchise.
   */
  @PatchMapping("/{id}/name")
  public Mono<Franchise> updateFranchiseName(
    @PathVariable String id,
    @RequestParam String name
  ) {
    return service.updateFranchiseName(id, name);
  }

  /**
   * Updates the name of a specific branch.
   */
  @PatchMapping("/{id}/branches/{branchId}/name")
  public Mono<Franchise> updateBranchName(
    @PathVariable String id,
    @PathVariable String branchId,
    @RequestParam String name
  ) {
    return service.updateBranchName(id, branchId, name);
  }

  /**
   * Updates the name of a specific product.
   */
  @PatchMapping("/{id}/branches/{branchId}/products/{productId}/name")
  public Mono<Franchise> updateProductName(
    @PathVariable String id,
    @PathVariable String branchId,
    @PathVariable String productId,
    @RequestParam String name
  ) {
    return service.updateProductName(id, branchId, productId, name);
  }
}
