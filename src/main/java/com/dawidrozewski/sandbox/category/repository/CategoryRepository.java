package com.dawidrozewski.sandbox.category.repository;

import com.dawidrozewski.sandbox.common.model.Category;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CategoryRepository extends JpaRepository<Category, Long> {

    Category findBySlug(String slug);


}
