package com.dawidrozewski.sandbox.category.controller;

import com.dawidrozewski.sandbox.category.controller.dto.CategoryProductDto;
import com.dawidrozewski.sandbox.category.service.CategoryService;
import com.dawidrozewski.sandbox.common.model.Category;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.data.domain.Pageable;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
@Validated
public class CategoryController {
    private final CategoryService categoryService;

    @GetMapping
    @Cacheable("categories")
    public List<Category> getCategories() {
        return categoryService.getCategories();
    }

    @GetMapping("/{slug}/products")
    @Cacheable("categoriesWithProducts")
    public CategoryProductDto getCategoriesWithProducts(@PathVariable
                                                    @Pattern(regexp = "[a-z0-9\\-]+")
                                                    @Length(max = 255)
                                                    String slug, Pageable pageable) {
        return categoryService.getCategoriesWithProducts(slug, pageable);
    }
}



