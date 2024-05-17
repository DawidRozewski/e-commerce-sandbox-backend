package com.dawidrozewski.sandbox.helper;

import com.dawidrozewski.sandbox.admin.category.model.AdminCategory;
import com.dawidrozewski.sandbox.admin.order.model.AdminOrder;
import com.dawidrozewski.sandbox.admin.order.model.AdminPayment;
import com.dawidrozewski.sandbox.admin.product.controller.dto.AdminProductDto;
import com.dawidrozewski.sandbox.admin.product.model.AdminProduct;
import com.dawidrozewski.sandbox.admin.product.model.AdminProductCurrency;
import com.dawidrozewski.sandbox.admin.review.model.AdminReview;
import com.dawidrozewski.sandbox.common.model.OrderStatus;
import com.dawidrozewski.sandbox.order.model.PaymentType;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public class AdminHelper {

    public static AdminPayment createAdminPayment(long id) {
        return AdminPayment.builder()
                .id(id)
                .note("note")
                .name("admin-payment-name")
                .type(PaymentType.BANK_TRANSFER)
                .defaultPayment(true)
                .build();
    }

    public static AdminOrder createAdminOrder(AdminPayment adminPayment, OrderStatus orderStatus, LocalDateTime placeDate) {
        return AdminOrder.builder()
                .placeDate(placeDate)
                .orderStatus(orderStatus)
                .orderRows(null)
                .grossValue(BigDecimal.TEN)
                .firstname("Jhon")
                .lastname("Doe")
                .street("Street name")
                .zipcode("62-200")
                .city("Poznan")
                .email("jhon@test.com")
                .phone("123456789")
                .payment(adminPayment)
                .orderLogs(null)
                .build();
    }

    public static AdminProduct createAdminProduct(String productName, Long categoryId, BigDecimal price, String slug) {
        return AdminProduct.builder()
                .categoryId(categoryId)
                .image("image-dump")
                .slug(slug)
                .description("description")
                .fullDescription("full-description")
                .currency(AdminProductCurrency.PLN)
                .price(price)
                .name(productName)
                .build();
    }

    public static AdminProductDto createAdminProductDto(String productName, Long categoryId, BigDecimal price, String slug) {
        return AdminProductDto.builder()
                .categoryId(categoryId)
                .image("image-dump")
                .slug(slug)
                .description("description")
                .fullDescription("full-description")
                .currency(AdminProductCurrency.PLN)
                .price(price)
                .name(productName)
                .build();
    }

    public static AdminCategory createAdminCategory(String categoryName, String slug) {
        return AdminCategory.builder()
                .name(categoryName)
                .slug(slug)
                .description("description")
                .build();
    }

    public static AdminReview createAdminReview(String authorName, boolean moderated, long productId) {
        return AdminReview.builder()
                .authorName(authorName)
                .content("some comment")
                .moderated(moderated)
                .productId(productId)
                .build();
    }
}
