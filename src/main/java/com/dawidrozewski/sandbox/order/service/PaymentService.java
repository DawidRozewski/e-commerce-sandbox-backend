package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.order.model.Payment;
import com.dawidrozewski.sandbox.order.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;

    public List<Payment> getPayments() {
        return paymentRepository.findAll();
    }
}
