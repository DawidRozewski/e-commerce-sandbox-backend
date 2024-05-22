package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.order.model.Payment;
import com.dawidrozewski.sandbox.order.repository.PaymentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.dawidrozewski.sandbox.helper.Helper.createPayment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class PaymentServiceTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentService paymentService;

    @Test
    void shouldReturnAllPayments() {
        //Given
        Payment payment = createPayment();
        when(paymentRepository.findAll()).thenReturn(List.of(payment));

        //When
        List<Payment> result = paymentService.getPayments();

        //Then
        verify(paymentRepository).findAll();
        assertEquals(1, result.size());
        assertEquals(payment.getId(), result.get(0).getId());
    }
}