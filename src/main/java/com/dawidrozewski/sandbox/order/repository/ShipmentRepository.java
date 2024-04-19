package com.dawidrozewski.sandbox.order.repository;

import com.dawidrozewski.sandbox.order.model.Shipment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ShipmentRepository extends JpaRepository<Shipment, Long> {
}
