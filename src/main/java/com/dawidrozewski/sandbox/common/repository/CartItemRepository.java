package com.dawidrozewski.sandbox.common.repository;

import com.dawidrozewski.sandbox.common.model.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CartItemRepository extends JpaRepository<CartItem, Long> {
    Long countByCartId(Long cartId);

    @Modifying
    @Query("delete from CartItem ci where ci.cartId in (:ids)")
    void deleteAllByCartIdIn(List<Long> ids);

    @Modifying
    @Query("delete from CartItem ci where ci.cartId=:cartId")
    void deleteByCartId(Long cartId);
}
