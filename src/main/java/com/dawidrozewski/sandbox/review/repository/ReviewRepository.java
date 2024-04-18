package com.dawidrozewski.sandbox.review.repository;

import com.dawidrozewski.sandbox.common.model.Review;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ReviewRepository extends JpaRepository<Review, Long> {

}
