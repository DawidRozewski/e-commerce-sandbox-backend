package com.dawidrozewski.sandbox.product.service;

import com.dawidrozewski.sandbox.product.model.Product;
import com.dawidrozewski.sandbox.product.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ProductService {
    private final ProductRepository productRepository;

    public List<Product> getProducts() {
        return productRepository.findAll();
    }
}
