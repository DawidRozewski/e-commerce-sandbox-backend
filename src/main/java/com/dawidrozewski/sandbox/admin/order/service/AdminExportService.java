package com.dawidrozewski.sandbox.admin.order.service;

import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrderStatus;
import com.dawidrozewski.sandbox.admin.order.repository.AdminOrderRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class AdminExportService {

    private final AdminOrderRepository orderRepository;

    public List<AdminOrder> exportOrders(LocalDateTime from, LocalDateTime to, AdminOrderStatus orderStatus) {
        return orderRepository.findAllByPlaceDateIsBetweenAndOrderStatus(from, to, orderStatus);
    }
}
