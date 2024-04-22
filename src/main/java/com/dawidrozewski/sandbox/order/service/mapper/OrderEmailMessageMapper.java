package com.dawidrozewski.sandbox.order.service.mapper;

import com.dawidrozewski.sandbox.order.model.Order;

import java.time.format.DateTimeFormatter;

public class OrderEmailMessageMapper {

    public static String createEmailMessage(Order order) {
        return "Your order id: " + order.getId() +
                "\n Order placed on: " + order.getPlaceDate().format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")) +
                "\n Total value: " + order.getGrossValue() + " PLN " +
                "\n\n" +
                "\n Payment method: " + order.getPayment().getName() +
                (order.getPayment().getNote() != null ? "\n" + order.getPayment().getNote() : "") +
                "\n\n Thank you for shopping with us.";
    }

}
