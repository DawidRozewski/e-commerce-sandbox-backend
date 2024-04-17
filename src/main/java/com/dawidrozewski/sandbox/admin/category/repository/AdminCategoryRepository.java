package com.dawidrozewski.sandbox.admin.category.repository;

import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminCategoryRepository extends JpaRepository<AdminCategory, Long> {
}
