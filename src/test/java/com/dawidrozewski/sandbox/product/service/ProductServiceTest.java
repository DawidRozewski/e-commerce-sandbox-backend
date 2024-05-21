package com.dawidrozewski.sandbox.product.service;

import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.model.Review;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import com.dawidrozewski.sandbox.common.repository.ReviewRepository;
import com.dawidrozewski.sandbox.helper.Helper;
import com.dawidrozewski.sandbox.product.service.dto.ProductDto;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyBoolean;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private ReviewRepository reviewRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void shouldGetProducts() {
        //Given
        Page<Product> productPage = mock(Page.class);
        Pageable pageable = mock(Pageable.class);

        when(productRepository.findAll(pageable)).thenReturn(productPage);

        //When
        Page<Product> result = productService.getProducts(pageable);

        //Then
        verify(productRepository).findAll(pageable);
        assertEquals(productPage, result);
    }

    @Test
    void shouldGetProduct() {
        //Given
        Category category = Helper.createCategory("category-slug");
        category.setId(1L);

        String productSlug = "product-slug";
        Product product = Helper.createProduct(category.getId(), new BigDecimal("5.00"), productSlug);
        product.setId(1L);

        Review review = Helper.createReview(product, true);
        when(productRepository.findBySlug(any())).thenReturn(Optional.of(product));
        when(reviewRepository.findAllByProductIdAndModerated(any(), anyBoolean())).thenReturn(List.of(review));

        //When
        ProductDto result = productService.getProduct(productSlug);

        //Then
        verify(productRepository).findBySlug(productSlug);
        verify(reviewRepository).findAllByProductIdAndModerated(1L, true);
        assertNotNull(result);
        assertEquals(productSlug, result.getSlug());
    }

    @Test
    void shouldThrowExceptionWhenProductNotFound() {
        // Given
        String nonExistentProductSlug = "nonexistent-product-slug";

        when(productRepository.findBySlug(nonExistentProductSlug)).thenReturn(Optional.empty());

        // When
        Exception exception = assertThrows(RuntimeException.class, () -> productService.getProduct(nonExistentProductSlug));

        // Then
        verify(productRepository).findBySlug(nonExistentProductSlug);
        assertNotNull(exception);
    }

}