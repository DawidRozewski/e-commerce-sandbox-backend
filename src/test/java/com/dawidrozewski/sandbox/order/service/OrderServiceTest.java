package com.dawidrozewski.sandbox.order.service;

import com.dawidrozewski.sandbox.common.mail.EmailClientService;
import com.dawidrozewski.sandbox.common.mail.FakeEmailService;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import com.dawidrozewski.sandbox.helper.Helper;
import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.PaymentType;
import com.dawidrozewski.sandbox.order.model.ShipmentType;
import com.dawidrozewski.sandbox.order.model.dto.OrderDto;
import com.dawidrozewski.sandbox.order.model.dto.OrderListDto;
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

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static com.dawidrozewski.sandbox.helper.Helper.createItems;
import static com.dawidrozewski.sandbox.helper.Helper.createOrderDto;
import static com.dawidrozewski.sandbox.helper.Helper.createOrdersForUser;
import static com.dawidrozewski.sandbox.helper.Helper.createPayment ;
import static com.dawidrozewski.sandbox.helper.Helper.createShipment;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
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
        when(cartRepository.findById(any())).thenReturn(Optional.of(Helper.createCart(createItems(), LocalDateTime.now().minusDays(5))));
        when(shipmentRepository.findById(any())).thenReturn(Optional.of(createShipment(2L, true, ShipmentType.DELIVERYMAN)));
        when(paymentRepository.findById(any())).thenReturn(Optional.of(createPayment()));
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(emailClientService.getInstance()).thenReturn(new FakeEmailService());
        Long userId = 1L;

        //When
        OrderSummary result = orderService.placeOrder(orderDto, userId);

        //Then
        assertNotNull(result);
        assertEquals(OrderStatus.NEW, result.getStatus());
        assertEquals(PaymentType.BANK_TRANSFER, result.getPayment().getType());
        assertEquals("bank transfer", result.getPayment().getName());
        assertEquals(1L, result.getPayment().getId());

    }

    @Test
    void shouldThrowExceptionWhenCartNotFound() {
        //Given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(orderDto, 1L));
    }

    @Test
    void shouldThrowExceptionWhenShipmentNotFound() {
        //Given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(Optional.of(Helper.createCart(createItems(), LocalDateTime.now().minusDays(5))));
        when(shipmentRepository.findById(any())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(orderDto, 1L));
    }

    @Test
    void shouldThrowExceptionWhenPaymentNotFound() {
        //Given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(Optional.of(Helper.createCart(createItems(), LocalDateTime.now().minusDays(5))));
        when(shipmentRepository.findById(any())).thenReturn(Optional.of(createShipment(2L, true, ShipmentType.DELIVERYMAN)));
        when(paymentRepository.findById(any())).thenReturn(Optional.empty());

        //When & Then
        assertThrows(NoSuchElementException.class, () -> orderService.placeOrder(orderDto, 1L));
    }

    @Test
    void shouldClearOrderCartAfterPlaceOrder() {
        //Given
        OrderDto orderDto = createOrderDto();
        when(cartRepository.findById(any())).thenReturn(Optional.of(Helper.createCart(createItems(), LocalDateTime.now().minusDays(5))));
        when(shipmentRepository.findById(any())).thenReturn(Optional.of(createShipment(2L, true, ShipmentType.DELIVERYMAN)));
        when(paymentRepository.findById(any())).thenReturn(Optional.of(createPayment()));
        when(orderRepository.save(any())).thenAnswer(invocationOnMock -> invocationOnMock.getArguments()[0]);
        when(emailClientService.getInstance()).thenReturn(new FakeEmailService());

        //When
        orderService.placeOrder(orderDto, 1L);

        //Then
        verify(cartRepository, times(1)).deleteCartById(any());
    }

    @Test
    void shouldReturnOrdersForCustomer() {
        //Given
        Long userId = 1L;
        List<Order> orders = createOrdersForUser(userId);
        when(orderRepository.findByUserId(userId)).thenReturn(orders);

        //When
        List<OrderListDto> result = orderService.getOrdersForCustomer(userId);

        //Then
        assertNotNull(result);
        assertEquals(orders.get(0).getId(), result.get(0).getId());
        assertEquals(orders.get(1).getId(), result.get(1).getId());
    }
}