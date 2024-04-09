package com.dawidrozewski.sandbox.product.repository;

import com.dawidrozewski.sandbox.product.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {

}
