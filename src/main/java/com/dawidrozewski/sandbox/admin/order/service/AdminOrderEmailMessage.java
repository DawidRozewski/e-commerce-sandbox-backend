package com.dawidrozewski.sandbox.admin.order.service;

import com.dawidrozewski.sandbox.common.model.OrderStatus;

public class AdminOrderEmailMessage {
    public static String creatingProcessingEmailMessage(Long id, OrderStatus newStatus) {
        return "Your order: " + id + " is being processed." +
                "\n The status has been changed to: " + newStatus.getValue() +
                "\n Your order is being processed by our staff" +
                "\n When completed, we will immediately forward it for shipment" +
                "\n\n Best regards" +
                "\n Sandbox store";
    }

    public static String createCompletedEmailMessage(Long id, OrderStatus newStatus) {
        return "Your order: " + id + " has been completed." +
                "\n The status has been changed to: " + newStatus.getValue() +
                "\n Thank you for your purchase and welcome back." +
                "\n\n Best regards" +
                "\n Sandbox store";
    }

    public static String createRefundEmail(Long id, OrderStatus newStatus) {
        return "Your order: " + id + " has been refunded." +
                "\n The status has been changed to: " + newStatus.getValue() +
                "\n\n Best regards" +
                "\n Sandbox store";
    }
}
