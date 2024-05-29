package com.dawidrozewski.sandbox.admin.order.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.admin.category.service.AdminCategoryService;
import com.dawidrozewski.sandbox.admin.order.controller.dto.AdminInitDataDto;
import com.dawidrozewski.sandbox.admin.order.controller.dto.AdminOrderDto;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminPayment;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderRepository;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import com.dawidrozewski.sandbox.helper.PageResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminOrder;
import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.patch;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class AdminOrderControllerTest extends AbstractConfiguredTest {

    @Autowired
    private AdminCategoryService adminCategoryService;

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    private AdminOrder order1;
    private AdminOrder order2;

    @BeforeEach
    void setUp() {
        AdminPayment payment1 = createAdminPayment(220L);
        order1 = createAdminOrder(payment1, OrderStatus.NEW, LocalDateTime.now().minusDays(1));
        adminOrderRepository.save(order1);

        AdminPayment payment2 = createAdminPayment(440L);
        order2 = createAdminOrder(payment2, OrderStatus.PROCESSING, LocalDateTime.now().minusDays(1));
        adminOrderRepository.save(order2);
    }

    @AfterEach
    void cleanUp() {
        adminOrderRepository.deleteAll();
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnAllOrders() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/orders"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        PageResponse<AdminOrderDto> returnedOrders = objectMapper.readValue(contentAsString, new TypeReference<>() {});

        assertEquals(2, returnedOrders.getTotalElements());
        assertEquals(order2.getId(), returnedOrders.getContent().get(0).getId());
        assertEquals(order1.getId(), returnedOrders.getContent().get(1).getId());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void shouldReturnOrderById() throws Exception {
        //Given
        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/orders/{id}", order1.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminOrder result = objectMapper.readValue(contentAsString, AdminOrder.class);
        assertEquals(result.getId(), order1.getId());
        assertEquals(result.getEmail(), order1.getEmail());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Transactional
    void shouldUpdateOrderStatus() throws Exception {
        //Given
        Map<String, String> values = new HashMap<>();
        values.put("orderStatus", "PAID");

        //When
        mockMvc.perform(patch("/admin/orders/{id}", order1.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(values)))
                .andExpect(status().isOk());

        //Then
        AdminOrder updatedOrder = adminOrderRepository.findById(order1.getId()).get();
        assertEquals(updatedOrder.getId(), order1.getId());
        assertEquals(OrderStatus.PAID, updatedOrder.getOrderStatus());
    }

    @WithMockUser(roles = "ADMIN")
    @Test
    void shouldReturnMapWithOrderStatuses() throws Exception {
        //Given

        //When
        MvcResult mvcResult = mockMvc.perform(get("/admin/orders/initData"))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminInitDataDto adminInitDataDto = objectMapper.readValue(contentAsString, AdminInitDataDto.class);
        assertNotNull(adminInitDataDto.getOrderStatuses());
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.NEW.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.PAID.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.PROCESSING.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.WAITING_FOR_DELIVERY.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.COMPLETED.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.CANCELED.name()));
        assertTrue(adminInitDataDto.getOrderStatuses().containsKey(OrderStatus.REFUND.name()));
        assertEquals(7, adminInitDataDto.getOrderStatuses().size());
    }
}