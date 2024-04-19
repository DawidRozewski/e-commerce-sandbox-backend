package com.dawidrozewski.sandbox.order.model.dto;

import com.dawidrozewski.sandbox.order.model.Shipment;
import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Getter
@Builder
public class InitOrder {
    private List<Shipment> shipment;
}
