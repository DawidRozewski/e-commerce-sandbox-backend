package com.dawidrozewski.sandbox.product.service;

import com.dawidrozewski.sandbox.product.model.Product;
import com.dawidrozewski.sandbox.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public Page<Product> getProducts(Pageable pageable) {
        return productRepository.findAll(pageable);
    }

    public Product getProduct(String slug) {
        return productRepository.findBySlug(slug).orElseThrow();
    }
}
