package com.dawidrozewski.sandbox.homepage.service;

import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HomepageService {

    private final ProductRepository productRepository;

    public List<Product> getSaleProducts() {
        return productRepository.findTop10BySalePriceIsNotNull();
    }

}
