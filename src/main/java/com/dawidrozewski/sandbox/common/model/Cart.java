package com.dawidrozewski.sandbox.common.model;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@Entity
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Cart {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private LocalDateTime created;
    @OneToMany(cascade = CascadeType.PERSIST)
    @JoinColumn(name = "cartId")
    @Setter
    private List<CartItem> items;

    public void addProduct(CartItem cartItem) {
        if (items == null) {
            items = new ArrayList<>();
        }

        items.stream()
                .filter(item -> Objects.equals(cartItem.getProduct().getId(), item.getProduct().getId()))
                .findFirst()
                .ifPresentOrElse(
                        item -> item.setQuantity(item.getQuantity() + 1),
                        () -> items.add(cartItem)
                );
    }
}
