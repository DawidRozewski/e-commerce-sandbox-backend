package com.dawidrozewski.sandbox.order.repository;

import com.dawidrozewski.sandbox.order.model.OrderRow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRowRepository extends JpaRepository<OrderRow, Long> {
}
