package com.dawidrozewski.sandbox.category.service;

import com.dawidrozewski.sandbox.category.model.Category;
import com.dawidrozewski.sandbox.category.model.CategoryProductDto;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.product.model.Product;
import com.dawidrozewski.sandbox.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
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
        return new CategoryProductDto(category, page);
    }
}
