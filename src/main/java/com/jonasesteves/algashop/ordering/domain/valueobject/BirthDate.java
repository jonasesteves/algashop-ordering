package com.jonasesteves.algashop.ordering.domain.valueobject;

import java.time.LocalDate;
import java.util.Objects;

import static com.jonasesteves.algashop.ordering.domain.exception.ErrorMessages.VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST;

public record BirthDate(LocalDate value) {

    public BirthDate(LocalDate value) {
        Objects.requireNonNull(value);
        if (value.isAfter(LocalDate.now())) {
            throw new IllegalArgumentException(VALIDATION_ERROR_BIRTHDATE_MUST_IN_PAST);
        }
        this.value = value;
    }

    public Integer age() {
        return LocalDate.now().getYear() - this.value().getYear();
    }

    @Override
    public String toString() {
        return this.value().toString();
    }
}
