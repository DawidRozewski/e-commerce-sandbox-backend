package com.dawidrozewski.sandbox.cart.controller;

import com.dawidrozewski.sandbox.cart.service.CartItemService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/cartItems")
public class CartItemController {

    private final CartItemService cartItemService;

    @DeleteMapping("/{id}")
    public void deleteCartItem(@PathVariable Long id) {
        cartItemService.delete(id);
    }

    @GetMapping("/count/{cartId}")
    public Long countItemInCart(@PathVariable Long cartId) {
        return cartItemService.countItemInCart(cartId);
    }

}
