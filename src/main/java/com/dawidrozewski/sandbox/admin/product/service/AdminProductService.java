package com.dawidrozewski.sandbox.admin.product.service;

import com.dawidrozewski.sandbox.admin.product.model.AdminProduct;
import com.dawidrozewski.sandbox.admin.product.repository.AdminProductRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AdminProductService {

    private final AdminProductRepository adminProductRepository;

    public Page<AdminProduct> getProducts(Pageable pageable) {
        return adminProductRepository.findAll(pageable);
    }
}
