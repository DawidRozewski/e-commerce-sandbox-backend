package com.dawidrozewski.sandbox.review.service;

import com.dawidrozewski.sandbox.common.model.Review;
import com.dawidrozewski.sandbox.common.repository.ReviewRepository;
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
