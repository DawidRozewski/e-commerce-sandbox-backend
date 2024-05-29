package com.dawidrozewski.sandbox.admin.review.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.admin.review.model.AdminReview;
import com.dawidrozewski.sandbox.admin.review.repository.AdminReviewRepository;
import com.dawidrozewski.sandbox.admin.review.service.AdminReviewService;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminReview;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminReviewControllerTest extends AbstractConfiguredTest {

    @Autowired
    private AdminReviewService adminReviewService;

    @Autowired
    private AdminReviewRepository adminReviewRepository;

    private AdminReview review;
    private AdminReview review2;

    @BeforeEach
    void setUp() {
        adminReviewRepository.deleteAll();
        review = createAdminReview("Jhon", true, 1L);
        review2 = createAdminReview("Doe", false, 2L);
        adminReviewRepository.save(review);
        adminReviewRepository.save(review2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnReviews() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/reviews"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<AdminReview> returnedReviews = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertEquals(2, returnedReviews.size());
        assertEquals(review.getId(), returnedReviews.get(0).getId());
        assertEquals(review2.getId(), returnedReviews.get(1).getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional(propagation = Propagation.NOT_SUPPORTED)
    void shouldChangeModeratedValueToTrue() throws Exception {
        //Given
        //When
        mockMvc.perform(put("/admin/reviews/{id}/moderate", review2.getId()))
                .andExpect(status().isOk());

        //Then
        review2 = adminReviewRepository.findById(review2.getId()).orElseThrow();
        assertTrue(review2.isModerated());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete() throws Exception {
        //Given
        //When
        mockMvc.perform(MockMvcRequestBuilders.delete("/admin/reviews/{id}", review2.getId()))
                .andExpect(status().isOk());
        //Then
        List<AdminReview> reviews = adminReviewRepository.findAll();
        assertEquals(1, reviews.size());
        assertEquals(review.getId(), reviews.get(0).getId());
    }
}