package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class ProductNameTest {

    @Test
    void shouldInstanceCorrectly() {
        ProductName productName = new ProductName("Any name");
        Assertions.assertThat(productName.value()).isEqualTo("Any name");
        Assertions.assertThat(productName).hasToString("Any name");
    }

    @Test
    void shouldNotGenerateWithEmptyValue() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new ProductName(""));
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new ProductName(" "));
    }

}