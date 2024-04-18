package com.dawidrozewski.sandbox.cart.repository;

import com.dawidrozewski.sandbox.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Long countByCartId(Long cartId);
}
