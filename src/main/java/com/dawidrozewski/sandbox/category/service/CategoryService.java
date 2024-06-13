package com.dawidrozewski.sandbox.category.service;

import com.dawidrozewski.sandbox.category.controller.dto.CategoryProductDto;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.common.dto.ProductListDto;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;
    private final ProductRepository productRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

    @Transactional(readOnly = true)
    public CategoryProductDto getCategoriesWithProducts(String slug, Pageable pageable) {
        Category category = categoryRepository.findBySlug(slug);
        Page<Product> page = productRepository.findByCategoryId(category.getId(), pageable);
        List<ProductListDto> productListDtos = page.getContent().stream()
                .map(product -> ProductListDto.builder()
                        .id(product.getId())
                        .name(product.getName())
                        .description(product.getDescription())
                        .price(product.getPrice())
                        .salePrice(product.getSalePrice())
                        .currency(product.getCurrency())
                        .image(product.getImage())
                        .slug(product.getSlug())
                        .build()
                ).toList();

        return new CategoryProductDto(category, new PageImpl<>(productListDtos, pageable, page.getTotalElements()));
    }
}
