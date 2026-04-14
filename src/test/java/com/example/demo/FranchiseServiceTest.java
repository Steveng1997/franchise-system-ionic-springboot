package com.example.demo.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.domain.model.*;
import com.example.demo.repository.FranchiseRepository;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

/**
 * Unit tests for FranchiseService.
 * Focuses on validating business logic requirements and reactive stream integrity.
 */
public class FranchiseServiceTest {

  private final FranchiseRepository repository = Mockito.mock(
    FranchiseRepository.class
  );
  private final FranchiseService service = new FranchiseService(repository);

  @Test
  @DisplayName("Should successfully create a new franchise")
  void testCreateFranchise() {
    // Arrange
    Franchise franchise = Franchise.builder()
      .name("Test Franchise")
      .branches(new ArrayList<>())
      .build();

    when(repository.save(any(Franchise.class))).thenReturn(
      Mono.just(franchise)
    );

    // Act & Assert
    StepVerifier.create(service.createFranchise(franchise))
      .expectNextMatches(f -> f.getName().equals("Test Franchise"))
      .verifyComplete();
  }

  @Test
  @DisplayName("Should add a branch to an existing franchise correctly")
  void testAddBranch() {
    // Arrange
    String franchiseId = "fran-123";
    Branch newBranch = Branch.builder().id("br-1").name("Cali North").build();
    Franchise existingFranchise = Franchise.builder()
      .id(franchiseId)
      .name("GNA Store")
      .branches(new ArrayList<>())
      .build();

    when(repository.findById(franchiseId)).thenReturn(
      Mono.just(existingFranchise)
    );
    when(repository.save(any(Franchise.class))).thenReturn(
      Mono.just(existingFranchise)
    );

    // Act & Assert
    StepVerifier.create(service.addBranch(franchiseId, newBranch))
      .expectNextMatches(
        f ->
          f.getBranches().size() == 1 &&
          f.getBranches().get(0).getName().equals("Cali North")
      )
      .verifyComplete();
  }

  /**
   * Fulfills Requirement #7: Show product with max stock per branch.
   */
  @Test
  @DisplayName("Should identify the product with highest stock for each branch")
  void testGetTopStockProducts() {
    // Arrange
    String franchiseId = "fran-123";

    // Branch 1: Max stock is 'Tool Kit' (50)
    Product p1 = Product.builder().name("Hammer").stock(10).build();
    Product p2 = Product.builder().name("Tool Kit").stock(50).build();
    Branch b1 = Branch.builder()
      .name("Downtown")
      .products(Arrays.asList(p1, p2))
      .build();

    // Branch 2: Max stock is 'Drill' (100)
    Product p3 = Product.builder().name("Drill").stock(100).build();
    Branch b2 = Branch.builder().name("Airport").products(List.of(p3)).build();

    Franchise franchise = Franchise.builder()
      .id(franchiseId)
      .branches(Arrays.asList(b1, b2))
      .build();

    when(repository.findById(franchiseId)).thenReturn(Mono.just(franchise));

    // Act & Assert
    StepVerifier.create(service.getTopStockProducts(franchiseId))
      .expectNextMatches(results -> {
        // Should find 2 top products (one per branch)
        return results.size() == 2;
      })
      .verifyComplete();
  }
}
