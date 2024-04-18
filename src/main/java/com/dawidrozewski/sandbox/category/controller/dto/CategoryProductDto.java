package com.dawidrozewski.sandbox.category.controller.dto;

import com.dawidrozewski.sandbox.category.model.Category;
import com.dawidrozewski.sandbox.product.controller.dto.ProductListDto;
import com.dawidrozewski.sandbox.product.model.Product;
import org.springframework.data.domain.Page;

public record CategoryProductDto(Category category, Page<ProductListDto> products) {
}
