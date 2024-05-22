package com.dawidrozewski.sandbox.helper;

import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.model.CartItem;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.model.Review;
import com.dawidrozewski.sandbox.order.model.Order;
import com.dawidrozewski.sandbox.order.model.Payment;
import com.dawidrozewski.sandbox.order.model.PaymentType;
import com.dawidrozewski.sandbox.order.model.Shipment;
import com.dawidrozewski.sandbox.order.model.ShipmentType;
import com.dawidrozewski.sandbox.order.model.dto.OrderDto;
import com.dawidrozewski.sandbox.product.service.dto.ReviewDto;

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

    public static Cart createCart(List<CartItem> cartItemList, LocalDateTime created) {
        return Cart.builder()
                .items(cartItemList)
                .created(created)
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

    public static ReviewDto createReviewDto(Product product) {
        return ReviewDto.builder()
                .authorName("Jhon Doe")
                .productId(product.getId())
                .content("<p>test <b>content</b></p>")
                .moderate(true)
                .build();
    }

    public static Review createReview(Product product, boolean moderated) {
        return Review.builder()
                .authorName("Jhon")
                .content("test content")
                .productId(product.getId())
                .moderated(moderated)
                .build();
    }

    public static Shipment createShipment(long id, boolean defaultShipment, ShipmentType shipmentType) {
        return Shipment.builder()
                .id(id)
                .defaultShipment(defaultShipment)
                .price(new BigDecimal("10.00"))
                .type(shipmentType)
                .build();
    }

    public static Payment createPayment() {
        return Payment.builder()
                .id(1L)
                .type(PaymentType.BANK_TRANSFER)
                .name("bank transfer")
                .defaultPayment(true)
                .note("note")
                .build();
    }

    public static List<CartItem> createItems() {
        CartItem cartItem_1 = CartItem.builder()
                .id(1L)
                .cartId(1L)
                .quantity(1)
                .product(Product.builder()
                        .id(1L)
                        .price(new BigDecimal("11.11"))
                        .build())
                .build();

        CartItem cartItem_2 = CartItem.builder()
                .id(2L)
                .cartId(2L)
                .quantity(1)
                .product(Product.builder()
                        .id(2L)
                        .price(new BigDecimal("11.11"))
                        .build())
                .build();

        return List.of(cartItem_1, cartItem_2);
    }


    public static OrderDto createOrderDto() {
        return OrderDto.builder()
                .firstname("firstname")
                .lastname("lastname")
                .street("street")
                .zipcode("zipcode")
                .city("city")
                .email("email")
                .phone("phone")
                .cartId(1L)
                .shipmentId(2L)
                .paymentId(3L)
                .build();
    }

    public static List<Order> createOrdersForUser(Long userId) {
        Order order = Order.builder()
                .userId(userId)
                .orderRows(null)
                .payment(createPayment())
                .placeDate(LocalDateTime.now().minusDays(5))
                .grossValue(new BigDecimal("10.00"))
                .zipcode("62-200")
                .street("Poznanska")
                .city("Gniezno")
                .orderStatus(OrderStatus.NEW)
                .email("jhonDoe@test.com")
                .phone("871624124")
                .firstname("jhon")
                .lastname("doe")
                .build();
        Order order2 = Order.builder()
                .userId(userId)
                .orderRows(null)
                .payment(createPayment())
                .placeDate(LocalDateTime.now().minusDays(5))
                .grossValue(new BigDecimal("10.00"))
                .zipcode("62-200")
                .street("Poznanska")
                .city("Gniezno")
                .orderStatus(OrderStatus.NEW)
                .email("jhonDoe@test.com")
                .phone("871624124")
                .firstname("jhon")
                .lastname("doe")
                .build();

        return List.of(order, order2);
    }


}
