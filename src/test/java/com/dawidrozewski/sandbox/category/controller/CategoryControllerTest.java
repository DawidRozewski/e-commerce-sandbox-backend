package com.dawidrozewski.sandbox.category.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.category.service.CategoryService;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.dawidrozewski.sandbox.helper.Helper.createCategory;
import static com.dawidrozewski.sandbox.helper.Helper.createProduct;
import static org.hamcrest.Matchers.hasSize;
import static org.hamcrest.Matchers.is;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CategoryControllerTest extends AbstractConfiguredTest {

    @Autowired
    private CategoryService categoryService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    private Category category;
    private Category category2;

    @BeforeEach
    void setUp() {
        category = createCategory("category-slug");
        category2 = createCategory("category-slug-two");
        categoryRepository.save(category);
        categoryRepository.save(category2);
    }

    @Test
    void getCategories() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/categories"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<Category> returnedCategories = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertEquals(2, returnedCategories.size());
        assertEquals(category.getId(), returnedCategories.get(0).getId());
        assertEquals(category2.getId(), returnedCategories.get(1).getId());
    }

    @Test
    void getCategoriesWithProducts() throws Exception {
        //Given
        Product product = createProduct(category.getId(), new BigDecimal("5.00"), "product-slug");
        productRepository.save(product);

        Product product2 = createProduct(category2.getId(), new BigDecimal("10.00"), "product-slug-two");
        productRepository.save(product2);

        //When
        //Then
        mockMvc.perform(MockMvcRequestBuilders.get("/categories/{slug}/products", category.getSlug()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.category.id", is(category.getId().intValue())))
                .andExpect(jsonPath("$.products.content", hasSize(1)))
                .andExpect(jsonPath("$.products.content[0].id", is(product.getId().intValue())));
    }
}