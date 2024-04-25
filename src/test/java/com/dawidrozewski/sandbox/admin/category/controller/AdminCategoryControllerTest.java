package com.dawidrozewski.sandbox.admin.category.controller;

import com.dawidrozewski.sandbox.admin.AdminHelper;
import com.dawidrozewski.sandbox.admin.category.controller.dto.AdminCategoryDto;
import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import com.dawidrozewski.sandbox.admin.category.service.AdminCategoryService;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AdminCategoryControllerTest {

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private AdminCategoryService adminCategoryService;

    @InjectMocks
    private AdminCategoryController adminCategoryController;

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllCategories() throws Exception {
        //Given
        AdminCategory category1 = new AdminCategory(1L, "Category 1", "description 1", "category-1");
        AdminCategory category2 = new AdminCategory(2L, "Category 2", "description 2", "category-2");
        when(adminCategoryService.getCategories()).thenReturn(List.of(category1, category2));

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
        AdminCategory category1 = new AdminCategory(1L, "Category 1", "description 1", "category-1");
        when(adminCategoryService.getCategory(any())).thenReturn(category1);

        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/categories/{id}", 1L))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory adminCategory = objectMapper.readValue(contentAsString, AdminCategory.class);
        assertEquals(1L, adminCategory.getId());
        assertEquals(adminCategory.getName(), "Category 1");
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
        when(adminCategoryService.createCategory(any(AdminCategory.class))).thenReturn(AdminHelper.mapToEntity(adminCategoryDto, 1L));

        //When
        MvcResult mvcResult = mockMvc.perform(post("/admin/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminCategoryDto)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory adminCategory = objectMapper.readValue(contentAsString, AdminCategory.class);
        assertEquals(adminCategory.getName(), "Category to create");
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateCategoryAndReturnIt() throws Exception {
        // Given
        AdminCategoryDto adminCategoryDto = new AdminCategoryDto("Updated Category", "Updated description", "updated-slug");
        AdminCategory updatedCategory = AdminHelper.mapToEntity(adminCategoryDto, 1L);
        when(adminCategoryService.updateCategory(any(AdminCategory.class))).thenReturn(updatedCategory);

        // When
        MvcResult mvcResult = mockMvc.perform(put("/admin/categories/{id}", 1)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(adminCategoryDto)))
                .andExpect(status().isOk())
                .andReturn();

        // Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminCategory returnedCategory = new ObjectMapper().readValue(contentAsString, AdminCategory.class);
        assertEquals("Updated Category", returnedCategory.getName());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteCategory() throws Exception {
        // When
        mockMvc.perform(delete("/admin/categories/{id}", 1))
                .andExpect(status().isOk());

        // Then
        verify(adminCategoryService).deleteCategory(1L);
    }

    @Test
    void shouldDeniedAccessIfUserIsNotAdmin() throws Exception {
        mockMvc.perform(get("/admin/categories/"))
                .andExpect(status().isForbidden());

    }
}