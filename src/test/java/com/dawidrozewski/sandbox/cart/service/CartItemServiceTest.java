package com.dawidrozewski.sandbox.cart.service;

import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartItemServiceTest {

    @Mock
    private CartItemRepository cartItemRepository;

    @InjectMocks
    private CartItemService cartItemService;

    @Test
    void shouldDeleteCartItem() {
        //Given
        Long id = 1L;

        //When
        cartItemService.delete(id);

        //Then
        verify(cartItemRepository).deleteById(id);
    }

    @Test
    void shouldCountItemsInCart() {
        //Given
        Long cartId = 1L;
        when(cartItemRepository.countByCartId(cartId)).thenReturn(5L);

        //When
        Long itemCount = cartItemService.countItemInCart(cartId);

        //Then
        assertEquals(5L, itemCount);
        verify(cartItemRepository).countByCartId(cartId);
    }
}