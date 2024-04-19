package com.dawidrozewski.sandbox.cart.service;

import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.cart.model.dto.CartProductDto;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class CartServiceTest {
    @Mock
    private CartRepository cartRepository;
    @Mock
    private ProductRepository productRepository;

    @InjectMocks
    private CartService cartService;

    @Test
    void shouldAddProductToCartWhenCartIdNotExist() {
        //Given
        Long cartId = 0L;
        CartProductDto cartProductDto = new CartProductDto(1L, 1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder().id(1L).build()));
        when(cartRepository.save(any())).thenReturn(Cart.builder().id(1L).build());

        //When
        Cart result = cartService.addProductToCart(cartId, cartProductDto);

        //Then
        assertNotNull(result);
        assertEquals(result.getId(), 1L);
    }

    @Test
    void shouldAddProductToCartWhenCartIdExist() {
        //Given
        Long cartId = 1L;
        CartProductDto cartProductDto = new CartProductDto(1L, 1);
        when(productRepository.findById(1L)).thenReturn(Optional.of(Product.builder().id(1L).build()));
        when(cartRepository.findById(cartId)).thenReturn(Optional.of(Cart.builder().id(1L).build()));

        //When
        Cart result = cartService.addProductToCart(cartId, cartProductDto);

        //Then
        assertNotNull(result);
        assertEquals(result.getId(), 1L);
    }
}