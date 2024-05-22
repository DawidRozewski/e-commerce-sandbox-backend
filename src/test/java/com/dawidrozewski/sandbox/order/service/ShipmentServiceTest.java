package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.order.model.Shipment;
import com.dawidrozewski.sandbox.order.model.ShipmentType;
import com.dawidrozewski.sandbox.order.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;

import static com.dawidrozewski.sandbox.helper.Helper.createShipment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ShipmentServiceTest {

    @Mock
    private ShipmentRepository shipmentRepository;

    @InjectMocks
    private ShipmentService shipmentService;

    @Test
    void shouldReturnAllShipments() {
        //Given
        Shipment shipment = createShipment(1L, false, ShipmentType.SELFPICKUP);
        Shipment shipment2 = createShipment(2L, true, ShipmentType.DELIVERYMAN);
        when(shipmentRepository.findAll()).thenReturn(List.of(shipment, shipment2));

        //When
        List<Shipment> result = shipmentService.getShipments();

        //Then
        verify(shipmentRepository).findAll();
        assertEquals(2, result.size());
        assertEquals(shipment.getId(), result.get(0).getId());
        assertEquals(shipment2.getId(), result.get(1).getId());
    }
}