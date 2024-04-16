package com.dawidrozewski.sandbox.admin.product.controller.dto;

import lombok.Getter;

import java.math.BigDecimal;

@Getter
public class AdminProductDto {
    private String name;
    private String category;
    private String description;
    private BigDecimal price;
    private String currency;
}
