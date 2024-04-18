package com.dawidrozewski.sandbox.cart.controller;

import com.dawidrozewski.sandbox.cart.controller.dto.CartSummaryDto;
import com.dawidrozewski.sandbox.cart.controller.mapper.CartMapper;
import com.dawidrozewski.sandbox.cart.model.dto.CartProductDto;
import com.dawidrozewski.sandbox.cart.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/carts")
public class CartController {

    private final CartService cartService;

    @GetMapping("/{id}")
    public CartSummaryDto getCart(@PathVariable Long id) {
        return CartMapper.mapToCartSummary(cartService.getCart(id));
    }

    @PutMapping("/{id}")
    public CartSummaryDto addProductToCart(@PathVariable Long id, @RequestBody CartProductDto cartProductDto) {
        return CartMapper.mapToCartSummary(cartService.addProductToCart(id, cartProductDto));
    }
}
