package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.RoundingMode;

class MoneyTest {

    @Test
    void shouldInstanceMoney() {
        Money money = new Money(BigDecimal.valueOf(2));
        Assertions.assertThat(money.value()).isEqualTo(BigDecimal.valueOf(2.00).setScale(2, RoundingMode.HALF_EVEN));
        Assertions.assertThat(money.toString()).hasToString("2.00");
    }

    @Test
    void shouldInstanceMoneyWithMultipleDecimalPlaces() {
        Money money = new Money(BigDecimal.valueOf(2.59682));
        Assertions.assertThat(money.value()).isEqualTo(BigDecimal.valueOf(2.60).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void shouldInstanceMoneyWithString() {
        Money money = new Money("2.80");
        Assertions.assertThat(money.value()).isEqualTo(new BigDecimal("2.80").setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void shouldSumTwoMoneyValues() {
        Money moneyOne = new Money("2.80");
        Money moneyTwo = new Money(BigDecimal.valueOf(0.2));
        Assertions.assertThat(moneyOne.add(moneyTwo).value()).isEqualTo(BigDecimal.valueOf(3).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void givenNegativeValue_whenCreateMoney_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Money(BigDecimal.valueOf(-2.85)));
    }

    @Test
    void givenNegativeStringValue_whenCreateMoney_shouldGenerateException() {
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> new Money("-2.85"));
    }

    @Test
    void givenQuantityValue_whenMultiplyMoney_shouldMultiplyCorrectly() {
        Money money = new Money(BigDecimal.valueOf(10));
        Quantity quantity = new Quantity(2);
        Money multiplied = money.multiply(quantity);
        Assertions.assertThat(multiplied.value()).isEqualTo(BigDecimal.valueOf(20).setScale(2, RoundingMode.HALF_EVEN));
    }

    @Test
    void givenInvalidQuantityValue_whenMultiplyMoney_shouldGenerateException() {
        Money money = new Money(BigDecimal.valueOf(10));
        Quantity quantity = new Quantity(0);
        Assertions.assertThatExceptionOfType(IllegalArgumentException.class).isThrownBy(() -> money.multiply(quantity));
    }

    @Test
    void givenInvalidMoneyValue_whenDivide_shouldDivideCorrectly() {
        Money moneyOne = new Money("10");
        Money moneyTwo = new Money("2.5");
        Assertions.assertThat(moneyOne.divide(moneyTwo)).isEqualTo(new Money("4"));
    }

    @Test
    void shouldCompareEqualMoneyValues() {
        Money moneyOne = new Money("10.00");
        Money moneyTwo = new Money("10.00");
        Assertions.assertThat(moneyOne).isEqualByComparingTo(moneyTwo);
    }

    @Test
    void shouldCompareGreaterMoneyValue() {
        Money moneyOne = new Money("15.00");
        Money moneyTwo = new Money("10.00");
        Assertions.assertThat(moneyOne.compareTo(moneyTwo)).isGreaterThan(0);
    }

    @Test
    void shouldCompareSmallerMoneyValue() {
        Money moneyOne = new Money("5.00");
        Money moneyTwo = new Money("10.00");
        Assertions.assertThat(moneyOne.compareTo(moneyTwo)).isLessThan(0);
    }
}