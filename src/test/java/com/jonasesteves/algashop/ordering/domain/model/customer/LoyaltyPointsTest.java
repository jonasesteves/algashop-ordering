package com.jonasesteves.algashop.ordering.domain.model.customer;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

class LoyaltyPointsTest {

    @Test
    void shouldGenerate() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldAddValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);
        var loyaltyPointsAdded = loyaltyPoints.add(5);
        Assertions.assertThat(loyaltyPointsAdded.value()).isEqualTo(15);
    }

    @Test
    void shouldNotAddNegativeValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> loyaltyPoints.add(-5));
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

    @Test
    void shouldNotAddZeroValue() {
        LoyaltyPoints loyaltyPoints = new LoyaltyPoints(10);

        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> loyaltyPoints.add(0));
        Assertions.assertThat(loyaltyPoints.value()).isEqualTo(10);
    }

}