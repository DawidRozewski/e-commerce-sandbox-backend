package com.dawidrozewski.sandbox.admin.order.controller.dto;

import com.dawidrozewski.sandbox.admin.order.model.AdminOrderStatus;
import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Builder
public class AdminOrderDto {
    private Long id;
    private LocalDateTime placeDate;
    private AdminOrderStatus orderStatus;
    private BigDecimal grossValue;
}
