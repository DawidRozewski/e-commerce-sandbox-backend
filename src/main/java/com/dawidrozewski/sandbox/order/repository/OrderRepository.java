package com.dawidrozewski.sandbox.order.repository;

import com.dawidrozewski.sandbox.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
}
