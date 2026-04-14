package com.example.demo.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.*;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

/**
 * Root Aggregate representing a Franchise.
 * Persisted as a document in the MongoDB 'franchises' collection.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "franchises")
public class Franchise {

  @Id
  private String id;

  private String name;
  private List<Branch> branches = new ArrayList<>();
}
