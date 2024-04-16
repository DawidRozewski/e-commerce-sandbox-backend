package com.dawidrozewski.sandbox.admin.product.repository;

import com.dawidrozewski.sandbox.admin.product.model.AdminProduct;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminProductRepository extends JpaRepository<AdminProduct, Long> {

}
