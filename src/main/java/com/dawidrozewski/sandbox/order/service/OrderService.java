package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.common.mail.EmailClientService;
import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.Payment;
import com.dawidrozewski.sandbox.order.model.PaymentType;
import com.dawidrozewski.sandbox.order.model.Shipment;
import com.dawidrozewski.sandbox.order.model.dto.OrderDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderListDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderSummary;
import com.dawidrozewski.sandbox.order.repository.OrderRepository;
import com.dawidrozewski.sandbox.order.repository.OrderRowRepository;
import com.dawidrozewski.sandbox.order.repository.PaymentRepository;
import com.dawidrozewski.sandbox.order.repository.ShipmentRepository;
import com.dawidrozewski.sandbox.order.service.payment.p24.PaymentMethodP24;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static com.dawidrozewski.sandbox.order.service.mapper.OrderDtoMapper.mapToOrderListDto;
import static com.dawidrozewski.sandbox.order.service.mapper.OrderEmailMessageMapper.createEmailMessage;
import static com.dawidrozewski.sandbox.order.service.mapper.OrderMapper.createNewOrder;
import static com.dawidrozewski.sandbox.order.service.mapper.OrderMapper.createOrderSummary;
import static com.dawidrozewski.sandbox.order.service.mapper.OrderMapper.mapToOrderRow;
import static com.dawidrozewski.sandbox.order.service.mapper.OrderMapper.mapToOrderRowWithQuantity;

@Service
@Slf4j
@RequiredArgsConstructor
public class OrderService {

    private final OrderRepository orderRepository;
    private final OrderRowRepository orderRowRepository;
    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ShipmentRepository shipmentRepository;
    private final PaymentRepository paymentRepository;
    private final EmailClientService emailClientService;
    private final PaymentMethodP24 paymentMethodP24;

    @Transactional
    public OrderSummary placeOrder(OrderDto orderDto, Long userId) {
        Cart cart = cartRepository.findById(orderDto.getCartId()).orElseThrow();
        Shipment shipment = shipmentRepository.findById(orderDto.getShipmentId()).orElseThrow();
        Payment payment = paymentRepository.findById(orderDto.getPaymentId()).orElseThrow();
        Order newOrder = orderRepository.save(createNewOrder(orderDto, cart, shipment, payment, userId));
        saveOrderRows(cart, newOrder.getId(), shipment);
        clearOrderCart(orderDto);
        sendConfirmEmail(newOrder);
        String redirectUrl = initPaymentIfNeeded(newOrder);
        return createOrderSummary(newOrder, payment, redirectUrl);
    }

    private String initPaymentIfNeeded(Order newOrder) {
        if (newOrder.getPayment().getType() == PaymentType.P24_ONLINE) {
           return paymentMethodP24.initPayment(newOrder);
        }
        return null;
    }

    private void sendConfirmEmail(Order newOrder) {
        emailClientService.getInstance()
                .send(newOrder.getEmail(), "Your order has been accepted.", createEmailMessage(newOrder));
    }

    private void clearOrderCart(OrderDto orderDto) {
        cartItemRepository.deleteByCartId(orderDto.getCartId());
        cartRepository.deleteCartById(orderDto.getCartId());
    }

    private void saveOrderRows(Cart cart, Long orderId, Shipment shipment) {
        saveProductRows(cart, orderId);
        saveShipmentRow(orderId, shipment);
    }

    private void saveProductRows(Cart cart, Long orderId) {
        cart.getItems().stream()
                .map(cartItem -> mapToOrderRowWithQuantity(orderId, cartItem))
                .peek(orderRowRepository::save)
                .toList();
    }

    private void saveShipmentRow(Long orderId, Shipment shipment) {
        orderRowRepository.save(mapToOrderRow(orderId, shipment));
    }

    public List<OrderListDto> getOrdersForCustomer(Long userId) {
        return mapToOrderListDto(orderRepository.findByUserId(userId));
    }

}
