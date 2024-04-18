package com.dawidrozewski.sandbox.category.service;

import com.dawidrozewski.sandbox.category.model.Category;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public List<Category> getCategories() {
        return categoryRepository.findAll();
    }

}
