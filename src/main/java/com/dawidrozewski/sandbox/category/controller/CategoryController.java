package com.dawidrozewski.sandbox.category.controller;

import com.dawidrozewski.sandbox.category.model.Category;
import com.dawidrozewski.sandbox.category.service.CategoryService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class CategoryController {
    private final CategoryService categoryService;

    public List<Category> getCategories() {
        return categoryService.getCategories();
    }
}



