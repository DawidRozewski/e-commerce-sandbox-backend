package com.dawidrozewski.sandbox.admin.review.controller;

import com.dawidrozewski.sandbox.admin.review.model.AdminReview;
import com.dawidrozewski.sandbox.admin.review.service.AdminReviewService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/reviews")
public class AdminReviewController {

    private final AdminReviewService reviewService;

    @GetMapping
    public List<AdminReview> getReviews(){
        return reviewService.getReviews();
    }

    @PutMapping("/{id}/moderate")
    public void moderate(@PathVariable Long id){
        reviewService.moderate(id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id){
        reviewService.delete(id);
    }
}