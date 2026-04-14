package com.example.demo.domain.model;

import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Product {

  private String id;
  private String name;
  private int stock;
}
