package com.dawidrozewski.sandbox.admin.order.model;


import lombok.Getter;

@Getter
public enum AdminOrderStatus {
    NEW("New"),
    PAID("Paid"),
    PROCESSING("Processing"),
    WAITING_FOR_DELIVERY("Waiting for delivery"),
    COMPLETED("Completed"),
    CANCELED("Canceled"),
    REFUND("Refund");

    private String value;

    AdminOrderStatus(String value) {
        this.value = value;
    }

}
