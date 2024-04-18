package com.dawidrozewski.sandbox.cart.repository;

import com.dawidrozewski.sandbox.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CartRepository extends JpaRepository<Cart, Long> {
}
