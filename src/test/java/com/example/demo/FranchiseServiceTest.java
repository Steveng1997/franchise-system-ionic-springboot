package com.example.demo.application.service;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import com.example.demo.domain.model.Franchise;
import com.example.demo.repository.FranchiseRepository;
import java.util.ArrayList;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import reactor.core.publisher.Mono;
import reactor.test.StepVerifier;

public class FranchiseServiceTest {

  private final FranchiseRepository repository = Mockito.mock(
    FranchiseRepository.class
  );
  private final FranchiseService service = new FranchiseService(repository);

  @Test
  void testCreateFranchise() {
    Franchise franchise = Franchise.builder()
      .name("Franquicia Test")
      .branches(new ArrayList<>())
      .build();

    when(repository.save(any(Franchise.class))).thenReturn(
      Mono.just(franchise)
    );

    StepVerifier.create(service.createFranchise(franchise))
      .expectNextMatches(f -> f.getName().equals("Franquicia Test"))
      .verifyComplete();
  }
}
