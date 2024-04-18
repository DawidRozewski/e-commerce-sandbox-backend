package com.dawidrozewski.sandbox.review.service;

import com.dawidrozewski.sandbox.review.model.Review;
import com.dawidrozewski.sandbox.review.repository.ReviewRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class ReviewService {

    private final ReviewRepository reviewRepository;

    public Review addReview(Review review) {
        return reviewRepository.save(review);
    }
}
