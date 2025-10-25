package com.jonasesteves.algashop.ordering.domain.valueobject.id;

import com.jonasesteves.algashop.ordering.domain.utility.IdGenerator;
import io.hypersistence.tsid.TSID;

import java.util.Objects;

public record OrderId(TSID value) {

    public OrderId {
        Objects.requireNonNull(value);
    }

    public OrderId() {
        this(IdGenerator.generateTSID());
    }

    public OrderId(Long value) {
        this(TSID.from(value));
    }

    public OrderId(String value) {
        this(TSID.from(value));
    }

    @Override
    public String toString() {
        return value.toString();
    }
}
