package com.dawidrozewski.sandbox.order.service.mapper;

import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.dto.OrderListDto;

import java.util.List;

public class OrderDtoMapper {

    public static List<OrderListDto> mapToOrderListDto(List<Order> orders) {
        return orders.stream()
                .map(order -> new OrderListDto(
                        order.getId(),
                        order.getPlaceDate(),
                        order.getOrderStatus(),
                        order.getGrossValue()))
                .toList();
    }
}
