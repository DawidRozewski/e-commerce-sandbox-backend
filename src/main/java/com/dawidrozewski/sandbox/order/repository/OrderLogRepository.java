package com.dawidrozewski.sandbox.order.repository;

import com.dawidrozewski.sandbox.order.model.OrderLog;
import org.springframework.data.jpa.repository.JpaRepository;

public interface OrderLogRepository extends JpaRepository<OrderLog, Long> {
}
