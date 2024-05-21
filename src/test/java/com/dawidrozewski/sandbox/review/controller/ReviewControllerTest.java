package com.dawidrozewski.sandbox.review.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.model.Review;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import com.dawidrozewski.sandbox.product.service.dto.ReviewDto;
import com.dawidrozewski.sandbox.review.service.ReviewService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;

import static com.dawidrozewski.sandbox.helper.Helper.createCategory;
import static com.dawidrozewski.sandbox.helper.Helper.createProduct;
import static com.dawidrozewski.sandbox.helper.Helper.createReviewDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class ReviewControllerTest extends AbstractConfiguredTest {

    @Autowired
    private ReviewService reviewService;

    @Autowired
    private CategoryRepository categoryRepository;

    @Autowired
    private ProductRepository productRepository;

    @Test
    void shouldAddReviewWithClearedContent() throws Exception {
        //Given
        Category category = createCategory("category-slug");
        categoryRepository.save(category);

        Product product = createProduct(category.getId(), new BigDecimal("5.00"), "product-slug");
        productRepository.save(product);

        ReviewDto reviewDto = createReviewDto(product);

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.post("/review")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(reviewDto)))
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        Review returnedReview = objectMapper.readValue(contentAsString, Review.class);
        assertEquals(reviewDto.getAuthorName(), returnedReview.getAuthorName());
        assertEquals(reviewDto.getProductId(), returnedReview.getProductId());
        assertEquals("test content", returnedReview.getContent());
    }

}