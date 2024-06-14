package com.dawidrozewski.sandbox.order.controller;

import com.dawidrozewski.sandbox.common.model.OrderStatus;
import com.dawidrozewski.sandbox.order.controller.dto.NotificationDto;
import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.dto.InitOrder;
import com.dawidrozewski.sandbox.order.model.dto.NotificationReceiveDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderListDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderSummary;
import com.dawidrozewski.sandbox.order.service.OrderService;
import com.dawidrozewski.sandbox.order.service.PaymentService;
import com.dawidrozewski.sandbox.order.service.ShipmentService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.hibernate.validator.constraints.Length;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
@Validated
public class OrderController {

    private final OrderService orderService;
    private final ShipmentService shipmentService;
    private final PaymentService paymentService;

    @PostMapping
    public OrderSummary placeOrder(@RequestBody OrderDto orderDto, @AuthenticationPrincipal Long userId) {
        return orderService.placeOrder(orderDto, userId);
    }

    @GetMapping("/initData")
    public InitOrder initData() {
        return InitOrder.builder()
                .shipments(shipmentService.getShipments())
                .payments(paymentService.getPayments())
                .build();
    }

    @GetMapping
    public List<OrderListDto> getOrders(@AuthenticationPrincipal Long userId) {
        if(userId == null) {
            throw new IllegalArgumentException("User do not exist.");
        }
        return orderService.getOrdersForCustomer(userId);
    }

    @GetMapping("/notification/{orderHash}")
    public NotificationDto notificationShow(@PathVariable @Length(max = 12) String orderHash) {
       Order order = orderService.getOrderByOrderHash(orderHash);
        return new NotificationDto(order.getOrderStatus() == OrderStatus.PAID);
    }

    @PostMapping("/notification/{orderHash}")
    public void notificationReceive(@PathVariable @Length(max = 12) String orderHash,
                                    @RequestBody NotificationReceiveDto receiveDto,
                                    HttpServletRequest request) {
        orderService.receiveNotification(orderHash, receiveDto, request.getRemoteAddr());
    }

}
