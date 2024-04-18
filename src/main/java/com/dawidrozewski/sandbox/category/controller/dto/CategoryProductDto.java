package com.dawidrozewski.sandbox.category.controller.dto;

import com.dawidrozewski.sandbox.common.dto.ProductListDto;
import com.dawidrozewski.sandbox.common.model.Category;
import org.springframework.data.domain.Page;

public record CategoryProductDto(Category category, Page<ProductListDto> products) {
}
