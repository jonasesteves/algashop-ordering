package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class QuantityTest {

    @Test
    void shouldInstanceQuantity() {
        Quantity quantity = new Quantity(2);
        Assertions.assertThat(quantity.value()).isEqualTo(2);
        Assertions.assertThat(quantity.toString()).hasToString("2");
    }

    @Test
    void shouldNotInstanceNegativeQuantity() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Quantity(-2));
    }

    @Test
    void shouldAddCorrectly() {
        Quantity q1 = new Quantity(2);
        Quantity q2 = new Quantity(4);
        Assertions.assertThat(q1.add(q2).value()).isEqualTo(6);
    }

    @Test
    void shouldCompareCorrectly() {
        Quantity q1 = new Quantity(2);
        Quantity q2 = new Quantity(4);
        Assertions.assertThat(q1.compareTo(q2)).isLessThan(0);
        Assertions.assertThat(q2.compareTo(q1)).isGreaterThan(0);
    }

    @Test
    void shouldCompareCorrectlyEqual() {
        Quantity q1 = new Quantity(4);
        Quantity q2 = new Quantity(4);
        Assertions.assertThat(q1).isEqualByComparingTo(q2);
    }
}