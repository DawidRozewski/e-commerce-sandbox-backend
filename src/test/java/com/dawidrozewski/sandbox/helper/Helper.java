package com.dawidrozewski.sandbox.helper;

import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.model.CartItem;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public class Helper {

    public static CartItem createCartItem(Long cartId, int quantity, Product product) {
        return CartItem.builder()
                .cartId(cartId)
                .quantity(quantity)
                .product(product)
                .build();
    }

    public static Cart createCart(List<CartItem> cartItemList) {
        return Cart.builder()
                .items(cartItemList)
                .created(LocalDateTime.now().minusDays(5))
                .build();
    }

    public static Product createProduct(long categoryId,BigDecimal price, String slug) {
        return Product.builder()
                .price(price)
                .image("vans.jpg")
                .name("product")
                .slug(slug)
                .currency("PLN")
                .categoryId(categoryId)
                .description("Black shoes")
                .fullDescription("Black shoes, full description")
                .build();
    }

    public static Category createCategory(String slug) {
        return Category.builder()
                .name("category")
                .slug(slug)
                .description("category-description")
                .build();
    }
}
