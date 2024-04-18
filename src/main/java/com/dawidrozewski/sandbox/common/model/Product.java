package com.dawidrozewski.sandbox.common.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@Entity
@Getter
@RequiredArgsConstructor
public class Product {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String name;
    private Long categoryId;
    private String description;
    private String fullDescription;
    private BigDecimal price;
    private String currency;
    private String image;
    private String slug;
    @OneToMany
    @JoinColumn(name = "productId")
    private List<Review> reviews;
}
