package com.dawidrozewski.sandbox.cart.service;

import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;

import static com.dawidrozewski.sandbox.helper.Helper.createCart;
import static com.dawidrozewski.sandbox.helper.Helper.createItems;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartCleanupServiceTest {

    @Mock
    private CartRepository cartRepository;
    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartCleanupService cartCleanupService;

    @Test
    void shouldCleanupCartsCreatedMoreThanThreeDaysAgo() {
        //Given
        Cart cart = createCart(createItems(), LocalDateTime.now().minusDays(5));
        Cart cart2 = createCart(createItems(), LocalDateTime.now().minusDays(6));
        List<Cart> carts = List.of(cart, cart2);
        List<Long> ids = carts.stream()
                .map(Cart::getId)
                .toList();

        when(cartRepository.findByCreatedLessThan(any())).thenReturn(carts);

        //When
        cartCleanupService.cleanupOldCarts();

        //Then
        verify(cartItemRepository).deleteAllByCartIdIn(ids);
        verify(cartRepository).deleteAllByIdIn(ids);
    }

    @Test
    void shouldNotCleanupCartsCreatedLessThanThreeDaysAgo() {
        //Given
        when(cartRepository.findByCreatedLessThan(any())).thenReturn(List.of());

        //When
        cartCleanupService.cleanupOldCarts();

        //Then
        verify(cartItemRepository, never()).deleteAllByCartIdIn(any());
        verify(cartRepository, never()).deleteAllByIdIn(any());
    }
}