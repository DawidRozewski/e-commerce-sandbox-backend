package com.dawidrozewski.sandbox.admin.category.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.admin.category.controller.dto.AdminCategoryDto;
import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import com.dawidrozewski.sandbox.admin.category.repository.AdminCategoryRepository;
import com.dawidrozewski.sandbox.admin.category.service.AdminCategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminCategory;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminCategoryControllerTest extends AbstractConfiguredTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Autowired
    private AdminCategoryRepository adminCategoryRepository;

    private AdminCategory category1;
    private AdminCategory category2;

    @BeforeEach
    void setUp() {
        category1 = createAdminCategory("Category 1", "category-slug");
        category2 = createAdminCategory("Category 2","category2-slug");
        adminCategoryRepository.save(category1);
        adminCategoryRepository.save(category2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllCategories() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/categories"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        List<AdminCategory> returnedList = objectMapper.readValue(contentAsString, new TypeReference<>() {
        });
        assertEquals(2, returnedList.size());
        assertEquals(returnedList.get(0).getName(), "Category 1");
        assertEquals(returnedList.get(1).getName(), "Category 2");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnCategoryById() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/categories/{id}", category1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory result = objectMapper.readValue(contentAsString, AdminCategory.class);
        assertEquals(result.getId(), category1.getId());
        assertEquals(result.getName(), "Category 1");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateCategoryAndReturnIt() throws Exception {
        //Given
        AdminCategoryDto adminCategoryDto = AdminCategoryDto.builder()
                .name("Category to create")
                .description("test")
                .slug("category-to-create")
                .build();

        //When
        MvcResult mvcResult = mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminCategoryDto)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory adminCategory = objectMapper.readValue(contentAsString, AdminCategory.class);
        assertEquals("Category to create", adminCategory.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategoryAndReturnIt() throws Exception {
        // Given
        AdminCategoryDto adminCategoryDto = new AdminCategoryDto("Updated Category 1", "Updated description 1", "updated-slug");

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminCategoryDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory returnedCategory = new ObjectMapper().readValue(contentAsString, AdminCategory.class);
        assertEquals("Updated Category 1", returnedCategory.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCategory() throws Exception {
        //Given
        // When
        mockMvc.perform(delete("/admin/categories/{id}", category1.getId()))
                .andExpect(status().isOk());

        // Then
        List<AdminCategory> all = adminCategoryRepository.findAll();
        assertEquals(1, all.size());
        assertEquals("Category 2", all.get(0).getName());
    }

    @Test
    void shouldDeniedAccessIfUserIsNotAdmin() throws Exception {
        mockMvc.perform(get("/admin/categories/"))
                .andExpect(status().isForbidden());
    }
}