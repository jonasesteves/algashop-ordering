package com.jonasesteves.algashop.ordering.domain.model.valueobject;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Objects;

public record Money(BigDecimal value) implements Comparable<Money> {

    public static final Money ZERO = new Money(BigDecimal.ZERO);

    public Money(BigDecimal value) {
        Objects.requireNonNull(value);
        this.value = value.setScale(2, RoundingMode.HALF_EVEN);
        if (this.value.signum() < 0) {
            throw new IllegalArgumentException();
        }
    }

    public Money(String value) {
        this(new BigDecimal(value));
    }

    public Money multiply(Quantity quantity) {
        Objects.requireNonNull(quantity);
        if (quantity.value() < 1) {
            throw new IllegalArgumentException();
        }
        return new Money(this.value.multiply(new BigDecimal(quantity.value())));
    }

    public Money add(Money other) {
        Objects.requireNonNull(other);
        return new Money(this.value.add(other.value()));
    }

    public Money divide(Money other) {
        Objects.requireNonNull(other);
        return new Money(this.value.divide(other.value(), RoundingMode.HALF_EVEN));
    }

    @Override
    public int compareTo(Money o) {
        return this.value.compareTo(o.value);
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
