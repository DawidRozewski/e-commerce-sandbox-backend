package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.common.mail.EmailClientService;
import com.dawidrozewski.sandbox.common.mail.FakeEmailService;
import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.model.CartItem;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import com.dawidrozewski.sandbox.order.model.OrderStatus;
import com.dawidrozewski.sandbox.order.model.Payment;
import com.dawidrozewski.sandbox.order.model.PaymentType;
import com.dawidrozewski.sandbox.order.model.Shipment;
import com.dawidrozewski.sandbox.order.model.dto.OrderDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderSummary;
import com.dawidrozewski.sandbox.order.repository.OrderRepository;
import com.dawidrozewski.sandbox.order.repository.OrderRowRepository;
import com.dawidrozewski.sandbox.order.repository.PaymentRepository;
import com.dawidrozewski.sandbox.order.repository.ShipmentRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class OrderServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private ShipmentRepository shipmentRepository;
    @Mock
    private PaymentRepository paymentRepository;
    @Mock
    private OrderRepository orderRepository;
    @Mock
    private OrderRowRepository orderRowRepository;
    @Mock
    private CartItemRepository cartItemRepository;
    @Mock
    private EmailClientService emailClientService;

    @InjectMocks
    private OrderService orderService;


    @Test
    void shouldPlaceOrder() {
        //Given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(createCart());
        when(shipmentRepository.findById(any())).thenReturn(createShipment());
        when(paymentRepository.findById(any())).thenReturn(createPayment());
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(emailClientService.getInstance()).thenReturn(new FakeEmailService());
        Long userId = 1L;

        //When
        OrderSummary orderSummary = orderService.placeOrder(orderDto, userId);

        //Then
        assertNotNull(orderSummary);
        assertEquals(orderSummary.getStatus(), OrderStatus.NEW);
        assertEquals(orderSummary.getGrossValue(), new BigDecimal("36.22"));
        assertEquals(orderSummary.getPayment().getType(), PaymentType.BANK_TRANSFER);
        assertEquals(orderSummary.getPayment().getName(), "test payment");
        assertEquals(orderSummary.getPayment().getId(), 1L);

    }

    private Optional<Payment> createPayment() {
        return Optional.of(Payment.builder()
                .id(1L)
                .name("test payment")
                .type(PaymentType.BANK_TRANSFER)
                .defaultPayment(true)
                .build());
    }

    private Optional<Shipment> createShipment() {
        return Optional.of(Shipment.builder()
                .id(2L)
                .price(new BigDecimal("14.00"))
                .build());
    }

    private Optional<Cart> createCart() {
        return Optional.of(Cart.builder()
                .id(1L)
                .created(LocalDateTime.now())
                .items(createItems())
                .build());
    }

    private List<CartItem> createItems() {
        CartItem cartItem_1 = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .quantity(1)
                .product(Product.builder()
                        .id(1L)
                        .price(new BigDecimal("11.11"))
                        .build())
                .build();

        CartItem cartItem_2 = CartItem.builder()
                .id(2L)
                .cartId(2L)
                .quantity(1)
                .product(Product.builder()
                        .id(2L)
                        .price(new BigDecimal("11.11"))
                        .build())
                .build();

        return List.of(cartItem_1, cartItem_2);
    }

    private static OrderDto createOrderDto() {
        return OrderDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .street("street")
                .zipcode("zipcode")
                .city("city")
                .email("email")
                .phone("phone")
                .cartId(1L)
                .shipmentId(2L)
                .paymentId(3L)
                .build();
    }
}