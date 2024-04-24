package com.dawidrozewski.sandbox.admin.order.service;

import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.common.mail.EmailClientService;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import static com.dawidrozewski.sandbox.admin.order.service.AdminOrderEmailMessage.createCompletedEmailMessage;
import static com.dawidrozewski.sandbox.admin.order.service.AdminOrderEmailMessage.createRefundEmail;
import static com.dawidrozewski.sandbox.admin.order.service.AdminOrderEmailMessage.creatingProcessingEmailMessage;

@Service
@RequiredArgsConstructor
class EmailNotificationForStatusChange {

    private final EmailClientService emailClientService;

    void sendEmailNotification(OrderStatus newStatus, AdminOrder adminOrder) {
        if (newStatus == OrderStatus.PROCESSING) {
            sendEmail(adminOrder.getEmail(),
                    "Order " + adminOrder.getId() + " changed status for: " + newStatus.getValue(),
                    creatingProcessingEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.COMPLETED) {
            sendEmail(adminOrder.getEmail(),
                    "Order " + adminOrder.getId() + " has been completed " + newStatus.getValue(),
                    createCompletedEmailMessage(adminOrder.getId(), newStatus));
        } else if (newStatus == OrderStatus.REFUND) {
            sendEmail(adminOrder.getEmail(),
                    "Order " + adminOrder.getId() + " has been refund " + newStatus.getValue(),
                    createRefundEmail(adminOrder.getId(), newStatus));
        }
    }

    private void sendEmail(String email, String subject, String message) {
        emailClientService.getInstance().send(email, subject, message);
    }
}
