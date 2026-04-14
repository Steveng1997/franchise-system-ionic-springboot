package com.example.demo.domain.model;

import lombok.*;

/**
 * Domain model representing a Product within a Branch.
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private String id;
  private String name;
  private int stock;
}
