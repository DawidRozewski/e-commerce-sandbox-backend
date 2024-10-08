package com.dawidrozewski.sandbox.admin.order.controller.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Map;

@Getter
@AllArgsConstructor
@NoArgsConstructor
public class AdminInitDataDto {
    private Map<String, String> orderStatuses;
}
