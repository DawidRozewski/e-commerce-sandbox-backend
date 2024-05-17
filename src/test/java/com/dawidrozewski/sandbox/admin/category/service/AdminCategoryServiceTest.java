package com.dawidrozewski.sandbox.admin.category.service;

import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import com.dawidrozewski.sandbox.admin.category.repository.AdminCategoryRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminCategoryServiceTest {

    @Mock
    private AdminCategoryRepository adminCategoryRepository;

    @InjectMocks
    private AdminCategoryService adminCategoryService;

    @Test
    void shouldReturnAllCategories() {
        //Given
        AdminCategory category_1 = AdminCategory.builder()
                .id(1L)
                .name("Category-1")
                .slug("category-1")
                .build();
        AdminCategory category_2 = AdminCategory.builder()
                .id(2L)
                .name("Category-2")
                .slug("category-2")
                .build();

        when(adminCategoryRepository.findAll()).thenReturn(List.of(category_1, category_2));

        //When
        List<AdminCategory> categories = adminCategoryService.getCategories();

        // Then
        assertNotNull(categories);
        assertEquals(2, categories.size());
    }

    @Test
    void shouldReturnCategory() {
        //Given
        AdminCategory category_1 = AdminCategory.builder()
                .id(1L)
                .name("Category-1")
                .slug("category-1")
                .build();
        AdminCategory category_2 = AdminCategory.builder()
                .id(2L)
                .name("Category-2")
                .slug("category-2")
                .build();

        when(adminCategoryRepository.findById(any())).thenReturn(Optional.of(category_1));

        //When
        AdminCategory category = adminCategoryService.getCategory(1L);

        // Then
        assertNotNull(category);
        assertEquals("Category-1", category.getName());
        assertEquals("category-1", category.getSlug());
    }

    @Test
    void shouldThrowAnErrorWhileCategoryDoNotExist() {
        //Given
        //When
        // Then
        assertThrows(RuntimeException.class,     () -> adminCategoryService.getCategory(1L), "Category 1 do not exist.");
    }

    @Test
    void shouldCreateCategory() {
        //Given
        AdminCategory category_1 = AdminCategory.builder()
                .id(1L)
                .name("Category-1")
                .slug("category-1")
                .build();
        when(adminCategoryRepository.save(any(AdminCategory.class))).thenReturn(category_1);

        //When
        AdminCategory savedCategory = adminCategoryService.createCategory(category_1);

        //Then
        assertNotNull(savedCategory);
        assertEquals("Category-1", savedCategory.getName());
        assertEquals("category-1", savedCategory.getSlug());
    }

}