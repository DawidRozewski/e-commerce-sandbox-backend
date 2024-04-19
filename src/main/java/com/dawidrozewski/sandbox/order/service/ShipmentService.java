package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.order.model.Shipment;
import com.dawidrozewski.sandbox.order.repository.ShipmentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ShipmentService {

    private final ShipmentRepository shipmentRepository;

    public List<Shipment> getShipments() {
        return shipmentRepository.findAll();
    }
}
