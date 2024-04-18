package com.dawidrozewski.sandbox.cart.repository;

import com.dawidrozewski.sandbox.cart.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Long countByCartId(Long cartId);

    @Modifying
    @Query("delete from CartItem ci where ci.cartId in (:ids)")
    void deleteAllByCartIdIn(List<Long> ids);
}
