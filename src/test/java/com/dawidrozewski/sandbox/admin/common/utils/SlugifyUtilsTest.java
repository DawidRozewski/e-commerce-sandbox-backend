package com.dawidrozewski.sandbox.admin.common.utils;

import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SlugifyUtilsTest {

    @ParameterizedTest
    @CsvSource({
            "test test.png, test-test.png",
            "hello world.png, hello-world.png",
            "ąęśćżźńłó.png, aesczznlo.png",
            "Product 1.png, product-1.png",
            "Product - 1.png, product-1.png",
            "Product    -     1.png, product-1.png",
            "Product__1.png, product-1.png",
    })
    void shouldSlugifyFilename(String in, String out) {
        //Given
        //When
        String filename = SlugifyUtils.slugifyFileName(in);

        //Then
        assertEquals(filename, out);
    }

}