package com.dawidrozewski.sandbox.order.repository;

import com.dawidrozewski.sandbox.order.model.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentRepository extends JpaRepository<Payment, Long> {

}
