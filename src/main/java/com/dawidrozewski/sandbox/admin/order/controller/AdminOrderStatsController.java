package com.dawidrozewski.sandbox.admin.order.controller;

import com.dawidrozewski.sandbox.admin.order.model.dto.AdminOrderStats;
import com.dawidrozewski.sandbox.admin.order.service.AdminOrderStatsService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/admin/orders/stats")
public class AdminOrderStatsController {

    private final AdminOrderStatsService adminOrderStatsService;

    @GetMapping
    public AdminOrderStats getOrderStatistics() {
        return adminOrderStatsService.getStatistics();
    }

}
