package com.dawidrozewski.sandbox.admin.order.service;

import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.dto.AdminOrderStats;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrderStatsServiceTest {
    @Mock
    private AdminOrderRepository adminOrderRepository;

    @InjectMocks
    private AdminOrderStatsService adminOrderStatsService;

    @Test
    void getStatistics() {
        //Given
        LocalDateTime from = LocalDateTime.of(2024, 1, 1, 12, 12);
        LocalDateTime to = LocalDateTime.of(2024, 1, 10, 12, 12);
        AdminOrder order_1 = AdminOrder.builder().grossValue(new BigDecimal(5)).placeDate(from.plusDays(2)).build();
        AdminOrder order_2 = AdminOrder.builder().grossValue(new BigDecimal(7)).placeDate(from.plusDays(5)).build();
        BigDecimal totalSales = order_1.getGrossValue().add(order_2.getGrossValue());
        when(adminOrderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(any(), any(), any())).thenReturn(List.of(order_1, order_2));

        //When
        AdminOrderStats statistics = adminOrderStatsService.getStatistics(from, to);

        BigDecimal calculatedTotalSales = statistics.getSale().stream()
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        int totalOrders = statistics.getOrder().stream()
                .mapToInt(Long::intValue)
                .sum();
        //Then
        assertEquals(totalSales, calculatedTotalSales);
        assertEquals(2, totalOrders);
        assertEquals(10, statistics.getSale().size());
        assertEquals(10, statistics.getLabel().size());
        assertEquals(10, statistics.getOrder().size());
    }


}