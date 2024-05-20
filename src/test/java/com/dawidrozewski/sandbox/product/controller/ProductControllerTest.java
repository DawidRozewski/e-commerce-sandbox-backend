package com.dawidrozewski.sandbox.product.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.common.dto.ProductListDto;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import com.dawidrozewski.sandbox.helper.Helper;
import com.dawidrozewski.sandbox.helper.PageResponse;
import com.dawidrozewski.sandbox.product.service.ProductService;
import com.dawidrozewski.sandbox.product.service.dto.ProductDto;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.dawidrozewski.sandbox.helper.Helper.createCategory;
import static com.dawidrozewski.sandbox.helper.Helper.createProduct;
import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ProductControllerTest extends AbstractConfiguredTest {

    @Autowired
    private ProductService productService;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Category category;
    private Product product;
    private Product product2;

    @BeforeEach
    void setUp() {
        category = createCategory("category-slug");
        categoryRepository.save(category);

        product = createProduct(category.getId(), new BigDecimal("20.00"), "product-slug");
        product2 = createProduct(category.getId(), new BigDecimal("10.00"), "product-slug-two");
        productRepository.save(product);
        productRepository.save(product2);
    }

    @Test
    void shouldReturnProductListDtoPage() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/products"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PageResponse<ProductListDto> returnedProductListDto = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertEquals(2, returnedProductListDto.getTotalElements());
        assertEquals(product.getId(), returnedProductListDto.getContent().get(0).getId());
        assertEquals(product2.getId(), returnedProductListDto.getContent().get(1).getId());
    }

    @Test
    void shouldReturnProductDtoBySlug() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/products/{slug}", product.getSlug()))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        ProductDto returnedProductDto = objectMapper.readValue(contentAsString, ProductDto.class);
        assertEquals(product.getId(), returnedProductDto.getId());
        assertEquals(product.getSlug(), returnedProductDto.getSlug());
    }
}