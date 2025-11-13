package com.jonasesteves.algashop.ordering.domain.model.valueobject.id;

import com.jonasesteves.algashop.ordering.domain.model.utility.IdGenerator;

import java.util.Objects;
import java.util.UUID;

public record ShoppingCartId(UUID value) {

    public ShoppingCartId {
        Objects.requireNonNull(value);
    }

    public ShoppingCartId() {
        this(IdGenerator.generateTimeBasedUUID());
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
