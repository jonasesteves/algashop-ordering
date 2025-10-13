package com.jonasesteves.algashop.ordering.domain.valueobject;

import com.jonasesteves.algashop.ordering.domain.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record CustomerId(UUID value) {

    public CustomerId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    public CustomerId(UUID value) {
        Objects.requireNonNull(value);
        this.value = value;
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
