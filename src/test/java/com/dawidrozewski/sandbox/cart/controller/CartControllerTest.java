package com.dawidrozewski.sandbox.cart.controller;

import com.dawidrozewski.sandbox.AbstractConfiguredTest;
import com.dawidrozewski.sandbox.cart.controller.dto.CartSummaryDto;
import com.dawidrozewski.sandbox.cart.model.dto.CartProductDto;
import com.dawidrozewski.sandbox.cart.service.CartService;
import com.dawidrozewski.sandbox.category.repository.CategoryRepository;
import com.dawidrozewski.sandbox.common.model.Cart;
import com.dawidrozewski.sandbox.common.model.CartItem;
import com.dawidrozewski.sandbox.common.model.Category;
import com.dawidrozewski.sandbox.common.model.Product;
import com.dawidrozewski.sandbox.common.repository.CartItemRepository;
import com.dawidrozewski.sandbox.common.repository.CartRepository;
import com.dawidrozewski.sandbox.common.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MvcResult;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static com.dawidrozewski.sandbox.helper.Helper.createCart;
import static com.dawidrozewski.sandbox.helper.Helper.createCartItem;
import static com.dawidrozewski.sandbox.helper.Helper.createCategory;
import static com.dawidrozewski.sandbox.helper.Helper.createProduct;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

class CartControllerTest extends AbstractConfiguredTest {

    @Autowired
    private CartService cartService;

    @Autowired
    private CartRepository cartRepository;

    @Autowired
    private CartItemRepository cartItemRepository;

    @Autowired
    private ProductRepository productRepository;

    @Autowired
    private CategoryRepository categoryRepository;

    private Cart cart;
    private List<CartItem> cartItemList;
    private Product product;
    private Product product2;
    private Category category;

    @BeforeEach
    void setUp() {
        category = createCategory("category-slug");
        categoryRepository.save(category);

        product = createProduct(category.getId(), new BigDecimal("200.00"), "vans-shoes");
        product2 = createProduct(category.getId(), new BigDecimal("50.00"), "t-shirt");
        productRepository.save(product);
        productRepository.save(product2);

        cart = createCart(cartItemList);
        cartRepository.save(cart);

        CartItem cartItem = createCartItem(cart.getId(), 1, product);
        CartItem cartItem2 = createCartItem(cart.getId(), 1, product2);
        cartItemRepository.save(cartItem);
        cartItemRepository.save(cartItem2);

        cart.setItems(new ArrayList<>(Arrays.asList(cartItem, cartItem2)));
        cartRepository.save(cart);
    }

    @Test
    void shouldReturnCartSummaryDtoById() throws Exception {
        //Given
        BigDecimal priceOfTwoItems = product.getPrice().add(product2.getPrice());

        //When
        MvcResult mvcResult = mockMvc.perform(get("/carts/{id}", cart.getId()))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        CartSummaryDto returnedCartSummaryDto = objectMapper.readValue(contentAsString, CartSummaryDto.class);

        assertEquals(2, returnedCartSummaryDto.getItems().size());
        assertEquals(product.getSlug(), returnedCartSummaryDto.getItems().get(0).getProduct().getSlug());
        assertEquals(product2.getSlug(), returnedCartSummaryDto.getItems().get(1).getProduct().getSlug());
        assertEquals(priceOfTwoItems, returnedCartSummaryDto.getSummary().getGrossValue());
    }

    @Test
    void shouldAddProductToCart() throws Exception {
        //Given
        Product newProduct = createProduct(category.getId(), new BigDecimal("50.00"), "new-product");
        productRepository.save(newProduct);
        CartProductDto cartProductDto = new CartProductDto(newProduct.getId(), 1);

        BigDecimal totalValueOfProductsInCart = product.getPrice()
                .add(product2.getPrice())
                .add(newProduct.getPrice());

        //When
        MvcResult mvcResult = mockMvc.perform(put("/carts/{id}", cart.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartProductDto)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        CartSummaryDto returnedCartSummaryDto = objectMapper.readValue(contentAsString, CartSummaryDto.class);

        assertEquals(3, returnedCartSummaryDto.getItems().size());
        assertEquals(totalValueOfProductsInCart, returnedCartSummaryDto.getSummary().getGrossValue());
    }

    @Test
    void shouldUpdateCart() throws Exception {
        //Given
        CartProductDto cartProduct = new CartProductDto(product.getId(), 2);
        CartProductDto cartProduct2 = new CartProductDto(product2.getId(), 2);

        List<CartProductDto> cartProductDtos = Arrays.asList(cartProduct, cartProduct2);

        //When
        MvcResult mvcResult = mockMvc.perform(put("/carts/{id}/update", cart.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(cartProductDtos)))
                .andExpect(status().isOk())
                .andReturn();

        //Then
        String contentAsString = mvcResult.getResponse().getContentAsString();
        CartSummaryDto returnedCartSummaryDto = objectMapper.readValue(contentAsString, CartSummaryDto.class);

        assertEquals(2, returnedCartSummaryDto.getItems().size());
        assertEquals(2, returnedCartSummaryDto.getItems().get(0).getQuantity());
        assertEquals(2, returnedCartSummaryDto.getItems().get(1).getQuantity());
    }
}