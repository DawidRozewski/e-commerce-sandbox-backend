package com.dawidrozewski.sandbox.review.service;

import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.model.Review;
import com.dawidrozewski.sandbox.common.repository.ReviewRepository;
import com.dawidrozewski.sandbox.helper.Helper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ReviewServiceTest {

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ReviewService reviewService;

    @Test
    void shouldAddReview() {
        //Given
        Category category = Helper.createCategory("category-slug");
        category.setId(1L);

        Product product = Helper.createProduct(category.getId(), new BigDecimal("5.00"), "product-slug");
        product.setId(1L);

        Review review = Helper.createReview(product, true);
        when(reviewRepository.save(review)).thenReturn(review);

        //When
        Review result = reviewService.addReview(review);

        //Then
        verify(reviewRepository).save(review);
        assertEquals(review, result);
    }
}