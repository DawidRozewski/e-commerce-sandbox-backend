package com.dawidrozewski.sandbox.admin.review.repository;

import com.dawidrozewski.sandbox.admin.review.model.AdminReview;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface AdminReviewRepository extends JpaRepository<AdminReview, Long> {

    @Query("update AdminReview r set r.moderated=true where r.id=:id")
    @Modifying
    void moderate(Long id);
}
