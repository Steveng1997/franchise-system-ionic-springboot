package com.example.demo.domain.model;

import java.util.ArrayList;
import java.util.List;
import lombok.*;

/**
 * Domain model representing a Franchise Branch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Branch {

  private String id;
  private String name;
  private List<Product> products = new ArrayList<>();
}
