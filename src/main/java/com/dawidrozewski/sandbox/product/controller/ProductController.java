package com.dawidrozewski.sandbox.product.controller;

import com.dawidrozewski.sandbox.product.model.Product;
import com.dawidrozewski.sandbox.product.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class ProductController {
    private final ProductService productService;

    @GetMapping("/products")
    public Page<Product> getProducts(Pageable pageable) {
        return productService.getProducts(pageable);
    }
}
