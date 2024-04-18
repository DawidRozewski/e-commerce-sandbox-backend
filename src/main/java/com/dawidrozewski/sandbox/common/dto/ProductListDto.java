package com.dawidrozewski.sandbox.common.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;

@Builder
@Getter
public class ProductListDto {
    private Long id;
    private String name;
    private String description;
    private BigDecimal price;
    private String currency;
    private String image;
    private String slug;
}
