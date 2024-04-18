package com.dawidrozewski.sandbox.cart.service;

import com.dawidrozewski.sandbox.cart.repository.CartItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class CartItemService {

    private final CartItemRepository cartItemRepository;

    public void delete(Long id) {
        cartItemRepository.deleteById(id);
    }

    public Long countItemInCart(Long cartId) {
        return cartItemRepository.countByCartId(cartId);
    }
}
