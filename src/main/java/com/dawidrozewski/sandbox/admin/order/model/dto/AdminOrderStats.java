package com.dawidrozewski.sandbox.admin.order.model.dto;

import lombok.Builder;
import lombok.Getter;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Getter
@Builder
public class AdminOrderStats {
    private List<Integer> label;
    private List<BigDecimal> sale;
    private List<Long> order;
}
