package com.dawidrozewski.sandbox.admin.order.service;

import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrderLog;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderLogRepository;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderRepository;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminOrderServiceTest {
    @Mock
    private AdminOrderRepository adminOrderRepository;
    @Mock
    private AdminOrderLogRepository adminOrderLogRepository;
    @Mock
    private EmailNotificationForStatusChange emailNotificationForStatusChange;

    @InjectMocks
    private AdminOrderService adminOrderService;

    @Test
    void shouldReturnOrdersPage() {
        //Given
        AdminOrder order_1 = AdminOrder.builder()
                .id(1L)
                .placeDate(LocalDateTime.now().minusDays(5))
                .orderStatus(OrderStatus.COMPLETED)
                .grossValue(BigDecimal.valueOf(20.00))
                .build();
        AdminOrder order_2 = AdminOrder.builder()
                .id(2L)
                .placeDate(LocalDateTime.now().minusDays(5))
                .orderStatus(OrderStatus.PROCESSING)
                .grossValue(BigDecimal.valueOf(5.00))
                .build();
        PageRequest pageRequest = PageRequest.of(0, 5, Sort.by("id").descending());
        Page<AdminOrder> expectedPage = new PageImpl<>(List.of(order_1, order_2));
        when(adminOrderRepository.findAll(any(Pageable.class))).thenReturn(expectedPage);

        //When
        Page<AdminOrder> resultPage = adminOrderService.getOrders(pageRequest);

        //Then
        assertEquals(expectedPage, resultPage);
        verify(adminOrderRepository, times(1)).findAll(pageRequest);
    }

    @Test
    void shouldReturnOrderById() {
        //Given
        AdminOrder expectedOrder = AdminOrder.builder()
                .id(1L)
                .placeDate(LocalDateTime.now().minusDays(5))
                .orderStatus(OrderStatus.COMPLETED)
                .grossValue(BigDecimal.valueOf(20.00))
                .build();
        when(adminOrderRepository.findById(any())).thenReturn(Optional.of(expectedOrder));

        //When
        AdminOrder result = adminOrderService.getOrder(1L);

        //Then
        assertNotNull(result);
        assertEquals(1L, result.getId());
    }

    @Test
    void shouldThrowAnError() {
        assertThrows(RuntimeException.class, () -> adminOrderService.getOrder(1235321521L));
    }

    @Test
    void shouldProcessOrderStatusChange() {
        //Given
        AdminOrder order = AdminOrder.builder()
                .id(1L)
                .orderStatus(OrderStatus.PROCESSING)
                .build();
        when(adminOrderRepository.findById(any())).thenReturn(Optional.of(order));

        HashMap<String, String> values = new HashMap<>();
        values.put("orderStatus", "COMPLETED");

        //When
        adminOrderService.patchOrder(order.getId(), values);

        //Then
        assertEquals(OrderStatus.COMPLETED, order.getOrderStatus());
        verify(adminOrderLogRepository, times(1)).save(any(AdminOrderLog.class));
        verify(emailNotificationForStatusChange, times(1)).sendEmailNotification(OrderStatus.COMPLETED, order);
    }

    @Test
    void shouldNotProcessWhenNewStatusIsTheSameAsOldOne() {
        //Given
        AdminOrder order = AdminOrder.builder()
                .id(1L)
                .orderStatus(OrderStatus.PROCESSING)
                .build();
        when(adminOrderRepository.findById(any())).thenReturn(Optional.of(order));

        HashMap<String, String> values = new HashMap<>();
        values.put("orderStatus", "PROCESSING");

        //When
        adminOrderService.patchOrder(order.getId(), values);

        //Then
        assertEquals(OrderStatus.PROCESSING, order.getOrderStatus());
        verify(adminOrderRepository, never()).save(any(AdminOrder.class));
        verifyNoInteractions(emailNotificationForStatusChange);
    }
}