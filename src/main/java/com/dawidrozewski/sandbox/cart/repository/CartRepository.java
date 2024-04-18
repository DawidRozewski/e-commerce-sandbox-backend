package com.dawidrozewski.sandbox.cart.repository;

import com.dawidrozewski.sandbox.cart.model.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;

public interface CartRepository extends JpaRepository<Cart, Long> {
    List<Cart> findByCreatedLessThan(LocalDateTime localDateTime);

    @Modifying
    @Query("delete from Cart c where c.id in (:ids)")
    void deleteAllByIdIn(List<Long> ids);

}
