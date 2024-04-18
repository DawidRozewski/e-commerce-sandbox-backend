package com.dawidrozewski.sandbox.category.model;

import com.dawidrozewski.sandbox.product.model.Product;
import org.springframework.data.domain.Page;

public record CategoryProductDto(Category category, Page<Product> products) {
}
