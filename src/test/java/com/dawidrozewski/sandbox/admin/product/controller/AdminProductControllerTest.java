package com.dawidrozewski.sandbox.admin.product.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import com.dawidrozewski.sandbox.admin.category.repository.AdminCategoryRepository;
import com.dawidrozewski.sandbox.admin.product.controller.dto.AdminProductDto;
import com.dawidrozewski.sandbox.admin.product.controller.dto.UploadResponse;
import com.dawidrozewski.sandbox.admin.product.model.AdminProduct;
import com.dawidrozewski.sandbox.admin.product.repository.AdminProductRepository;
import com.dawidrozewski.sandbox.admin.product.service.AdminProductImageService;
import com.dawidrozewski.sandbox.admin.product.service.AdminProductService;
import com.dawidrozewski.sandbox.helper.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.util.List;

import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminCategory;
import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminProduct;
import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminProductDto;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminProductControllerTest extends AbstractConfiguredTest {

    @MockBean
    private AdminProductImageService adminProductImageService;

    @Autowired
    private AdminProductService adminProductService;

    @Autowired
    private AdminProductRepository adminProductRepository;

    @Autowired
    private AdminCategoryRepository adminCategoryRepository;

    private AdminProduct product;
    private AdminProduct product2;
    private AdminCategory category;

    @BeforeEach
    void setUp() {
        category = createAdminCategory("category", "slug");
        adminCategoryRepository.save(category);

        product = createAdminProduct("Product 1", category.getId(), new BigDecimal("5.00"), "product-slug");
        product2 = createAdminProduct("Product 2", category.getId(), new BigDecimal("10.00"), "product2-slug");

        adminProductRepository.save(product);
        adminProductRepository.save(product2);
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllProducts() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PageResponse<AdminProduct> returnedProducts = objectMapper.readValue(contentAsString, new TypeReference<>() {});
        assertEquals(product.getId(), returnedProducts.getContent().get(0).getId());
        assertEquals(product2.getId(), returnedProducts.getContent().get(1).getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnProduct() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/products/{id}", product.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminProduct returnedProducts = objectMapper.readValue(contentAsString, AdminProduct.class);
        assertEquals(returnedProducts.getId(), product.getId());
        assertEquals(returnedProducts.getName(), product.getName());
        assertEquals(returnedProducts.getCategoryId(), product.getCategoryId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldCreateProduct() throws Exception {
        //Given
        AdminProductDto productDto = createAdminProductDto(
                "Product",
                category.getId(),
                BigDecimal.TEN,
                "test-slug");

        //When
        MvcResult mvcResult = mockMvc.perform(post("/admin/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isCreated())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminProduct returnedProduct = objectMapper.readValue(contentAsString, AdminProduct.class);
        assertEquals(returnedProduct.getCategoryId(), productDto.getCategoryId());
        assertEquals(returnedProduct.getName(), productDto.getName());
        assertEquals(returnedProduct.getSlug(), productDto.getSlug());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUpdateProduct() throws Exception{
        //Given
        AdminProductDto productDto = createAdminProductDto(
                "Updated Product",
                category.getId(),
                BigDecimal.TEN,
                "updated-slug");

        //When
        MvcResult mvcResult = mockMvc.perform(put("/admin/products/{id}", product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(productDto)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminProduct returnedProduct = objectMapper.readValue(contentAsString, AdminProduct.class);
        assertEquals(returnedProduct.getId(), product.getId());
        assertEquals(returnedProduct.getName(), productDto.getName());
        assertEquals(returnedProduct.getSlug(), productDto.getSlug());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldDeleteProduct() throws Exception{
        //Given
        //When
        mockMvc.perform(delete("/admin/products/{id}", product.getId()))
                .andExpect(status().isOk());

        //Then
        List<AdminProduct> products = adminProductRepository.findAll();
        assertEquals(1, products.size());
        assertEquals(product2.getId(), products.get(0).getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldUploadImage() throws Exception{
        //Given
        String originalFilename = "image.jpeg";
        byte[] fileContent = "test image content".getBytes();
        MockMultipartFile multipartFile = new MockMultipartFile("file", originalFilename, "image/jpeg", fileContent);

        when(adminProductImageService.uploadImage(anyString(), any())).thenReturn(originalFilename);

        //When
        MvcResult mvcResult = mockMvc.perform(multipart("/admin/products/upload-image")
                        .file(multipartFile)
                        .contentType(MediaType.MULTIPART_FORM_DATA))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        UploadResponse uploadResponse = objectMapper.readValue(contentAsString, UploadResponse.class);
        assertEquals(originalFilename, uploadResponse.filename());
        verify(adminProductImageService).uploadImage(eq(originalFilename), any());
    }

    @Test
    void serveFiles() {
    }
}