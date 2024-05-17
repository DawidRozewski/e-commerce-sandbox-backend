package com.dawidrozewski.sandbox.admin.order.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminPayment;
import com.dawidrozewski.sandbox.admin.order.model.dto.AdminOrderStats;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderRepository;
import com.dawidrozewski.sandbox.admin.order.service.AdminOrderStatsService;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;

import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminOrder;
import static com.dawidrozewski.sandbox.helper.AdminHelper.createAdminPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


class AdminOrderStatsControllerTest extends AbstractConfiguredTest {

    @Autowired
    private AdminOrderStatsService adminOrderStatsService;

    @Autowired
    private AdminOrderRepository adminOrderRepository;

    @Test
    @WithMockUser(roles = "ADMIN")
    void getOrderStatistics() throws Exception {
        //Given
        LocalDateTime from = LocalDateTime.now().minusDays(10);
        LocalDateTime to = LocalDateTime.now();
        long daysOfOrderStatistic = ChronoUnit.DAYS.between(from.toLocalDate(), to.toLocalDate()) + 1;

        AdminPayment payment = createAdminPayment(70L);
        AdminOrder order = createAdminOrder(payment, OrderStatus.COMPLETED, from);
        order.setGrossValue(new BigDecimal("5.00"));
        adminOrderRepository.save(order);

        AdminOrder order2 = createAdminOrder(payment, OrderStatus.COMPLETED, from);
        order2.setGrossValue(new BigDecimal("10.00"));
        adminOrderRepository.save(order2);

        AdminOrder orderOutOfStatistic = createAdminOrder(payment, OrderStatus.COMPLETED, to.plusDays(1));
        adminOrderRepository.save(orderOutOfStatistic);

        //When
        MvcResult mvcResult = mockMvc.perform(MockMvcRequestBuilders.get("/admin/orders/stats/{from}/{to}", from, to))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        AdminOrderStats returnedStats = objectMapper.readValue(contentAsString, AdminOrderStats.class);
        assertEquals(daysOfOrderStatistic, returnedStats.getOrder().size());
        assertEquals(order.getGrossValue().add(order2.getGrossValue()), returnedStats.getSale().get(0));
    }
}